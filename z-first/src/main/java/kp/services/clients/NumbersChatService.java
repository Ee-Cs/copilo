package kp.services.clients;

import io.grpc.stub.StreamObserver;
import kp.proto.NumberNote;
import kp.proto.NumbersChatServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
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

    public boolean startNumbersChat(int limit) {

        final Queue<Integer> queue = new ConcurrentLinkedQueue<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final StreamObserver<NumberNote> responseObserver = createResponseObserver(queue, countDownLatch);
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

    private StreamObserver<NumberNote> createResponseObserver(
            Queue<Integer> queue, CountDownLatch countDownLatch) {

        return new StreamObserver<>() {
            @Override
            public void onNext(NumberNote numberNote) {
                queue.add(numberNote.getNumber());
                logger.info("StreamObserver ⬤ onNext(): number[{}]", numberNote.getNumber());
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error("StreamObserver ⬤ onError(): exception[{}]", throwable.getMessage());
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                logger.info("StreamObserver ⬤ onCompleted():");
                countDownLatch.countDown();
            }
        };
    }

    private boolean runNumbersChat(int limit, Queue<Integer> queue,
                                   StreamObserver<NumberNote> requestObserver) {

        final AtomicInteger atomic = new AtomicInteger();
        try {
            NumberNote numberNote = NumberNote.newBuilder().setNumber(START_NUMBER).build();
            logger.info("\n" + "#".repeat(150));
            requestObserver.onNext(numberNote);
            logger.info("runNumbersChat(): sent first number[{}]", numberNote.getNumber());
            if (atomic.get() == limit) {
                logger.warn("runNumbersChat(): no loop run, reached limit[{}]", limit);
            }
            while (atomic.get() < limit) {
                if (atomic.incrementAndGet() >= limit) {
                    logger.info("runNumbersChat(): stopping at limit[{}]", limit);
                    break;
                }
                final Integer receivedNumber = queue.poll();
                if (receivedNumber == null) {
                    logger.warn("runNumbersChat(): receivedNumber is null");
                    continue;
                }
                numberNote = NumberNote.newBuilder().setNumber(receivedNumber + 1).build();
                execute(requestObserver, numberNote);
                logger.info("runNumbersChat(): number[{}]", receivedNumber);
            }
        } catch (RuntimeException e) {
            logger.error("runNumbersChat(): RuntimeException[{}]", e.getMessage());
            requestObserver.onError(e);
            return false;
        }
        requestObserver.onCompleted();
        logger.info("runNumbersChat(): queue number[{}], completed", queue.peek());
        return true;
    }

    //FIXME ################################################################################
    private void execute(StreamObserver<NumberNote> requestObserver, NumberNote numberNote) {

        try {
            requestObserver.onNext(numberNote);
        } catch (Throwable e) {
            logger.error("execute(): exception[{}]", e.getMessage());
        }
    }
}
