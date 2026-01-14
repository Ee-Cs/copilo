package kp.services.clients;

import io.grpc.stub.StreamObserver;
import kp.proto.NumberNote;
import kp.proto.NumbersChatServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NumbersChatService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private final NumbersChatServiceGrpc.NumbersChatServiceStub numbersChatServiceStub;
    private static final int START_NUMBER = 1;

    public NumbersChatService(@Autowired NumbersChatServiceGrpc.NumbersChatServiceStub numbersChatServiceStub) {
        this.numbersChatServiceStub = numbersChatServiceStub;
    }

    CountDownLatch countDownLatch;

    public boolean startNumbersChat(int limit) {

        final Queue<Integer> queue = new ConcurrentLinkedQueue<>();
        countDownLatch = new CountDownLatch(1);
        final StreamObserver<NumberNote> responseObserver = createResponseObserver(queue);
        final StreamObserver<NumberNote> requestObserver = numbersChatServiceStub.numbersChat(responseObserver);
        boolean result = runNumbersChat(limit, queue, requestObserver);
        try {
            result = result && countDownLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("startNumbersChat(): InterruptedException[{}]", e.getMessage());
            return false;
        }
        return result;
    }

    private StreamObserver<NumberNote> createResponseObserver(Queue<Integer> queue) {

        return new StreamObserver<>() {
            @Override
            public void onNext(NumberNote numberNote) {
                queue.add(numberNote.getNumber());
                logger.info("StreamObserver::onNext(): number[{}]", numberNote.getNumber());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
                logger.error("StreamObserver::onError(): exception[{}] <<<<< COUNT WAS DECREMENTED", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                logger.info("StreamObserver::onCompleted():");
                countDownLatch.countDown();
            }
        };
    }

    private boolean runNumbersChat(int limit, Queue<Integer> queue,
                                   StreamObserver<NumberNote> requestObserver) {

        final AtomicInteger atomic = new AtomicInteger();
        try {
            NumberNote numberNote = NumberNote.newBuilder().setNumber(START_NUMBER).build();
            logger.info("\n" + "#".repeat(150) + " a");
            requestObserver.onNext(numberNote);
            logger.info("runNumbersChat(): sent first number[{}]", numberNote.getNumber());
            while (atomic.get() < limit) {
                if (countDownLatch.getCount() == 0) {
                    logger.error("runNumbersChat(): <<<<< LATCH COUNT IS ZERO --> BREAKING LOOP");
                    break;
                }
                if (atomic.incrementAndGet() >= limit) {
                    logger.info("runNumbersChat(): stopping at limit[{}]", limit);
                    break;
                }
                final Integer numberReceived = getReceivedNumber(queue);
                if (numberReceived == null) {
                    logger.warn("runNumbersChat(): received number is null");
                    continue;
                }
                final int numberSent = numberReceived + 1;
                numberNote = NumberNote.newBuilder().setNumber(numberSent).build();
                requestObserver.onNext(numberNote);
                logger.info("runNumbersChat(): received number[{}], sent number[{}]", numberReceived, numberSent);
            }
        } catch (Exception e) {
            logger.error("runNumbersChat(): exception[{}]", e.getMessage());
            requestObserver.onError(e);
            return false;
        }
        requestObserver.onCompleted();
        logger.info("runNumbersChat(): completed");
        return true;
    }

    private Integer getReceivedNumber(Queue<Integer> queue) {
        final Instant start = Instant.now();
        logger.error("getReceivedNumber(): countDownLatch[{}] %%%%% queue.peek B-E-F-O-R-E %%%%%", countDownLatch.getCount());
        boolean flag = queue.peek() == null;
        try {
            for (int counter = 0; flag || counter < 30000; counter++) {
                Thread.sleep(1);
                flag = flag && queue.peek() == null;
            }
        } catch (InterruptedException e) {
            //ignore
        }
        logger.error("getReceivedNumber(): countDownLatch[{}] %%%%% queue.peek A-F-T-E-R %%%%% time elapsed[{}]",
                countDownLatch.getCount(), Duration.between(start, Instant.now()).toSeconds());
        return queue.poll();
    }
}
