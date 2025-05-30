package kp.services.clients;

import io.grpc.stub.StreamObserver;
import kp.proto.WordNote;
import kp.proto.WordsChatServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

/**
 * Service for bidirectional chat with the gRPC server handling words.
 */
@Service
public class WordsChatService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    @GrpcClient("grpcServerOnSecond")
    private WordsChatServiceGrpc.WordsChatServiceStub wordsChatServiceStub;

    private static final String START_WORD = "AAA";
    private static final UnaryOperator<String> WORD_FUN = str -> Optional.ofNullable(str)
            .map(arg -> arg.codePoints().map(cp -> cp + 1)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString())
            .orElse("");

    /**
     * Starts a chat session with the specified limit.
     *
     * @param limit the upper bound for the number of exchanges (inclusive)
     * @return {@code true} if the chat completes successfully; {@code false} otherwise
     */
    public boolean startWordsChat(int limit) {

        final Queue<String> queue = new ConcurrentLinkedQueue<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final StreamObserver<WordNote> responseObserver = createResponseObserver(queue, countDownLatch);
        final StreamObserver<WordNote> requestObserver = wordsChatServiceStub.wordsChat(responseObserver);
        boolean completed = runWordsChat(limit, queue, requestObserver);
        try {
            completed = completed && countDownLatch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("startWordsChat(): InterruptedException[{}]", e.getMessage());
            return false;
        }
        return completed;
    }

    /**
     * Creates a {@link StreamObserver} to handle responses from the server.
     *
     * @param queue          the queue to collect received words
     * @param countDownLatch latch to signal completion
     * @return the response observer
     */
    private StreamObserver<WordNote> createResponseObserver(
            Queue<String> queue, CountDownLatch countDownLatch) {

        return new StreamObserver<>() {
            /**
             * {@inheritDoc}
             */
            @Override
            public void onNext(WordNote wordNote) {
                queue.add(wordNote.getWord());
                logger.info("StreamObserver.onNext(): received word[{}]", wordNote.getWord());
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void onError(Throwable t) {
                countDownLatch.countDown();
                logger.error("StreamObserver.onError(): exception[{}]", t.getMessage());
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
     * Sends and receives words via gRPC, incrementing until the limit is reached.
     *
     * @param limit           the maximum number of exchanges
     * @param queue           the queue for received words
     * @param requestObserver the observer for sending requests
     * @return {@code true} if the chat ran without exception; {@code false} otherwise
     */
    private boolean runWordsChat(int limit, Queue<String> queue,
                                 StreamObserver<WordNote> requestObserver) {

        final AtomicInteger atomic = new AtomicInteger();
        try {
            WordNote wordNote = WordNote.newBuilder().setWord(START_WORD).build();
            requestObserver.onNext(wordNote);
            logger.info("runWordsChat(): sent first word[{}]", wordNote.getWord());
            while (atomic.get() < limit) {
                final String receivedWord = queue.poll();
                if (receivedWord != null) {
                    if (atomic.incrementAndGet() >= limit) {
                        logger.info("runWordsChat(): stopping at limit[{}], last word[{}]",
                                limit, receivedWord);
                        break;
                    }
                    final String nextWord = WORD_FUN.apply(receivedWord);
                    wordNote = WordNote.newBuilder().setWord(nextWord).build();
                    requestObserver.onNext(wordNote);
                    logger.info("runWordsChat(): sent next word[{}]", wordNote.getWord());
                }
            }
        } catch (RuntimeException e) {
            logger.error("runWordsChat(): RuntimeException[{}]", e.getMessage());
            requestObserver.onError(e);
            return false;
        }
        requestObserver.onCompleted();
        return true;
    }
}
