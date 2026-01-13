package kp.services.servers;
import io.grpc.stub.StreamObserver;
import kp.proto.NumberNote;
import kp.proto.NumbersChatServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;

@GrpcService
public class NumbersChatServiceGrpcImpl extends NumbersChatServiceGrpc.NumbersChatServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Override
    public StreamObserver<NumberNote> numbersChat(StreamObserver<NumberNote> numbersNoteObserver) {
        return new NumbersChatStreamObserver(numbersNoteObserver);
    }

    private record NumbersChatStreamObserver(StreamObserver<NumberNote> numbersNoteObserver)
            implements StreamObserver<NumberNote> {
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

        public void onError(Throwable throwable) {
            logger.error("onError(): numbers chat encountered an exception[{}]", throwable.getMessage());
        }

        @Override
        public void onCompleted() {
            numbersNoteObserver.onCompleted();
            logger.info("onCompleted(): numbers chat completed");
        }
    }
}