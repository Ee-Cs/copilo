package kp.services.clients;
import io.grpc.stub.StreamObserver;
import kp.proto.NumberNote;
import kp.proto.NumbersChatServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @GrpcClient("grpcServerOnSecond")
    private NumbersChatServiceGrpc.NumbersChatServiceStub numbersChatServiceStub;

    private static final int START_NUMBER = 1;

    public boolean startNumbersChat(int limit) {

        final Queue<Integer> queue = new ConcurrentLinkedQueue<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final StreamObserver<NumberNote> responseObserver = createResponseObserver(queue, countDownLatch);
        final StreamObserver<NumberNote> requestObserver = numbersChatServiceStub.numbersChat(responseObserver);
        boolean result = runNumbersChat(limit, queue, requestObserver);
        try {
            result = result && countDownLatch.await(1, TimeUnit.MINUTES);
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
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
                logger.error("StreamObserver.onError(): exception[{}]", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
                logger.info("StreamObserver.onCompleted():");
            }
        };
    }

    private boolean runNumbersChat(int limit, Queue<Integer> queue,
                                   StreamObserver<NumberNote> requestObserver) {

        final AtomicInteger atomic = new AtomicInteger();
        try {
            NumberNote numberNote = NumberNote.newBuilder().setNumber(START_NUMBER).build();
            requestObserver.onNext(numberNote);
            logger.info("runNumbersChat(): sent first number[{}]", numberNote.getNumber());
            while (atomic.get() < limit) {
                final Integer receivedNumber = queue.poll();
                if (receivedNumber != null) {
                    if (atomic.incrementAndGet() >= limit) {
                        logger.info("runNumbersChat(): stopping at limit[{}], last number[{}]",
                                limit, receivedNumber);
                        break;
                    }
                    numberNote = NumberNote.newBuilder().setNumber(receivedNumber + 1).build();
                    requestObserver.onNext(numberNote);
                }
            }
        } catch (RuntimeException e) {
            logger.error("runNumbersChat(): RuntimeException[{}]", e.getMessage());
            requestObserver.onError(e);
            return false;
        }
        requestObserver.onCompleted();
        return true;
    }
}
