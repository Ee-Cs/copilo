package kp.services.servers;

import io.grpc.stub.StreamObserver;
import kp.proto.WordNote;
import kp.proto.WordsChatServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * gRPC service implementation for a words chat.
 * <p>
 * This service is registered with the gRPC server and
 * handles bidirectional streaming of {@link WordNote} messages.
 * Each received word is changed and sent back to the client.
 * </p>
 */
@GrpcService
public class WordsChatServiceGrpcImpl extends WordsChatServiceGrpc.WordsChatServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    /**
     * Increments each Unicode code point in the input string by one.
     * If the input string is {@code null}, returns an empty string.
     */
    private static final UnaryOperator<String> WORD_FUN = str -> Optional.ofNullable(str)
            .map(arg -> arg.codePoints().map(cp -> cp + 1)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString())
            .orElse("");

    /**
     * Handles bidirectional streaming of word notes.
     *
     * @param wordsNoteObserver the observer to send responses to
     * @return a {@link StreamObserver} for receiving {@link WordNote} messages from the client
     */
    @Override
    public StreamObserver<WordNote> wordsChat(StreamObserver<WordNote> wordsNoteObserver) {
        return new WordsChatStreamObserver(wordsNoteObserver);
    }

    /**
     * StreamObserver implementation for handling words chat logic.
     * <p>
     * Each received {@link WordNote} is incremented and returned.
     * </p>
     */
    private record WordsChatStreamObserver(StreamObserver<WordNote> wordsNoteObserver)
            implements StreamObserver<WordNote> {
        /**
         * Processes the next {@link WordNote} from the client.
         *
         * @param wordNoteReceived the received note
         */
        @Override
        public void onNext(WordNote wordNoteReceived) {

            final WordNote wordNoteSent = WordNote.newBuilder()
                    .setWord(WORD_FUN.apply(wordNoteReceived.getWord())).build();
            wordsNoteObserver.onNext(wordNoteSent);
            logger.info("onNext(): received word [{}], sent word [{}]",
                    wordNoteReceived.getWord(), wordNoteSent.getWord()
            );
        }

        /**
         * Handles errors that occur during the stream.
         *
         * @param throwable the throwable describing the error
         */
        @Override
        public void onError(Throwable throwable) {
            logger.error("onError(): words chat encountered an exception[{}]", throwable.getMessage());
        }

        /**
         * Called when the stream is completed.
         */
        @Override
        public void onCompleted() {
            wordsNoteObserver.onCompleted();
            logger.info("onCompleted(): words chat completed");
        }
    }
}