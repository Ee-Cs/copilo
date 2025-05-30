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

/**
 * Service for bidirectional chat with the gRPC server handling numbers.
 */
@Service
public class NumbersChatService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    @GrpcClient("grpcServerOnSecond")
    private NumbersChatServiceGrpc.NumbersChatServiceStub numbersChatServiceStub;

    private static final int START_NUMBER = 1;
    // With stop number 10_000_000: 15 minutes running

    /**
     * Starts a chat session with the specified limit.
     *
     * @param limit the upper bound for the number exchange (inclusive)
     * @return true if the chat completes successfully; false otherwise
     */
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

    /**
     * Creates a {@link StreamObserver} to handle responses from the server.
     *
     * @param queue          the queue to collect received numbers
     * @param countDownLatch latch to signal completion
     * @return the response observer
     */
    private StreamObserver<NumberNote> createResponseObserver(
            Queue<Integer> queue, CountDownLatch countDownLatch) {

        return new StreamObserver<>() {
            /**
             * {@inheritDoc}
             */
            @Override
            public void onNext(NumberNote numberNote) {
                queue.add(numberNote.getNumber());
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
                logger.error("StreamObserver.onError(): exception[{}]", throwable.getMessage());
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void onCompleted() {
                countDownLatch.countDown();
                logger.info("StreamObserver.onCompleted():");
            }
        };
    }

    /**
     * Sends and receives numbers via gRPC, incrementing until the limit is reached.
     *
     * @param limit           the maximum count
     * @param queue           the queue to receive numbers from the server
     * @param requestObserver the observer to send requests
     * @return true if completed normally; false if an exception occurred
     */
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
