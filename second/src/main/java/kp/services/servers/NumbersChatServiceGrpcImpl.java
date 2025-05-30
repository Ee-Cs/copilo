package kp.services.servers;

import io.grpc.stub.StreamObserver;
import kp.proto.NumberNote;
import kp.proto.NumbersChatServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * gRPC service implementation for a numbers chat.
 * <p>
 * This service is registered with the gRPC server and
 * handles bidirectional streaming of {@link NumberNote} messages.
 * Each received number is incremented by one and sent back to the client.
 * Logging is performed for every 5000th number and for every number when debug logging is enabled.
 * </p>
 */
@GrpcService
public class NumbersChatServiceGrpcImpl extends NumbersChatServiceGrpc.NumbersChatServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    /**
     * Handles bidirectional streaming of number notes.
     *
     * @param numbersNoteObserver the observer to send responses to
     * @return a {@link StreamObserver} for receiving {@link NumberNote} messages from the client
     */
    @Override
    public StreamObserver<NumberNote> numbersChat(StreamObserver<NumberNote> numbersNoteObserver) {
        return new NumbersChatStreamObserver(numbersNoteObserver);
    }

    /**
     * StreamObserver implementation for handling numbers chat logic.
     * <p>
     * Each received {@link NumberNote} is incremented and returned.
     * </p>
     */
    private record NumbersChatStreamObserver(StreamObserver<NumberNote> numbersNoteObserver)
            implements StreamObserver<NumberNote> {
        /**
         * Processes the next {@link NumberNote} from the client.
         *
         * @param numberNoteReceived the received note
         */
        @Override
        public void onNext(NumberNote numberNoteReceived) {

            final NumberNote numberNoteSent = NumberNote.newBuilder()
                    .setNumber(numberNoteReceived.getNumber() + 1).build();
            numbersNoteObserver.onNext(numberNoteSent);
            if (numberNoteSent.getNumber() % 5000 == 0) {
                logger.info("onNext(): received number [{}], sent number [{}] (multiple of 5000)",
                        numberNoteReceived.getNumber(), numberNoteSent.getNumber());
            }
            if (logger.isDebugEnabled()) {
                logger.debug("onNext(): received number [{}], sent number [{}]",
                        numberNoteReceived.getNumber(), numberNoteSent.getNumber());
            }
        }

        /**
         * Handles errors that occur during the stream.
         *
         * @param throwable the throwable describing the error
         */
        @Override
        public void onError(Throwable throwable) {
            logger.error("onError(): numbers chat encountered an exception[{}]", throwable.getMessage());
        }

        /**
         * Called when the stream is completed.
         */
        @Override
        public void onCompleted() {
            numbersNoteObserver.onCompleted();
            logger.info("onCompleted(): numbers chat completed");
        }
    }
}