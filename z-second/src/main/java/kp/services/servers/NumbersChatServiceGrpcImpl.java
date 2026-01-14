package kp.services.servers;

import io.grpc.stub.StreamObserver;
import kp.proto.NumberNote;
import kp.proto.NumbersChatServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class NumbersChatServiceGrpcImpl extends NumbersChatServiceGrpc.NumbersChatServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Override
    public StreamObserver<NumberNote> numbersChat(StreamObserver<NumberNote> responseObserver) {
        return new NumbersChatStreamObserver(responseObserver);
    }

    private record NumbersChatStreamObserver(StreamObserver<NumberNote> responseObserver)
            implements StreamObserver<NumberNote> {
        @Override
        public void onNext(NumberNote numberNoteReceived) {
            if (numberNoteReceived.getNumber() == 1) {
                logger.info("\n" + "#".repeat(150));
            }
            final NumberNote numberNoteSent = NumberNote.newBuilder()
                    .setNumber(numberNoteReceived.getNumber() + 1).build();
            try {
                responseObserver.onNext(numberNoteSent);
            } catch (Throwable e) {
                logger.error("NumbersChatStreamObserver::onNext(): exception[{}]", e.getMessage());
            }
            logger.info("NumbersChatStreamObserver::onNext(): received number [{}], sent number [{}]",
                    numberNoteReceived.getNumber(), numberNoteSent.getNumber());
        }

        public void onError(Throwable throwable) {
            logger.error("NumbersChatStreamObserver::onError(): exception[{}]", throwable.getMessage());
        }

        @Override
        public void onCompleted() {
            responseObserver.onCompleted();
            logger.info("NumbersChatStreamObserver::onCompleted(): numbers chat completed");
        }
    }
}
