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
                logger.info("\n" + "#".repeat(150) + " b");
            }
            final NumberNote numberNoteSent = NumberNote.newBuilder()
                    .setNumber(numberNoteReceived.getNumber() + 1).build();
            try {
                responseObserver.onNext(numberNoteSent);
            } catch (Throwable e) {
                logger.error("NumbersChatStreamObserver::onNext(): exception[{}] <-<-<-<-<-<-<-<-", e.getMessage());
                e.printStackTrace();
                logger.info("\n" + "-".repeat(150));
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

    // FIXME ###############################################################################################
    @Override
    public void numbersSingleReply(NumberNote numberNoteReq,
                                   StreamObserver<NumberNote> responseObserver) {

        if (numberNoteReq.getNumber() == 1) {
            logger.info("\n" + "#".repeat(150));
        }
        final NumberNote numberNoteResp = NumberNote.newBuilder().setNumber(numberNoteReq.getNumber() + 1).build();
        responseObserver.onNext(numberNoteResp);
        responseObserver.onCompleted();
        logger.info("numbersSingleReply(): REQ number[{}], RESP number[{}]",
                numberNoteReq.getNumber(), numberNoteResp.getNumber());
    }

    // FIXME ###############################################################################################
    @Override
    public void numbersStreamServerReply(NumberNote numberNoteReq,
                                         StreamObserver<NumberNote> responseObserver) {

        if (numberNoteReq.getNumber() == 11) {
            logger.info("\n" + "#".repeat(150));
        }
        for (int index = 1; index <= 3; index++) {
            int number = numberNoteReq.getNumber() + index;
            final NumberNote numberNoteResp = NumberNote.newBuilder().setNumber(number).build();
            responseObserver.onNext(numberNoteResp);
            logger.info("numbersStreamServerReply(): REQ number[{}], RESP number[{}]",
                    numberNoteReq.getNumber(), numberNoteResp.getNumber());
        }
        responseObserver.onCompleted();
        logger.info("numbersStreamServerReply():");
    }

    // FIXME ###############################################################################################
    @Override
    public StreamObserver<NumberNote> numbersStreamClientReply(StreamObserver<NumberNote> responseObserver) {
        return new NumbersStreamClientObserver(responseObserver);
    }

    private record NumbersStreamClientObserver(StreamObserver<NumberNote> responseObserver)
            implements StreamObserver<NumberNote> {
        @Override
        public void onNext(NumberNote numberNoteReq) {
            if (numberNoteReq.getNumber() == 101) {
                logger.info("\n" + "#".repeat(150));
            }
            final NumberNote numberNoteResp = NumberNote.newBuilder().setNumber(numberNoteReq.getNumber() + 1).build();
//                ServerCallStreamObserver<NumberNote> serverCallStreamObserver = (ServerCallStreamObserver<NumberNote>)responseObserver;
//                if(serverCallStreamObserver.isCancelled()) {
//                    logger.error("NumbersStreamClientObserver::onNext(): cancelled cancelled cancelled cancelled");
//                    return;
//                }
//                logger.warn("NumbersStreamClientObserver::onNext(): isCancelled[{}], isReady[{}]",
//                        serverCallStreamObserver.isCancelled(), serverCallStreamObserver.isReady());
            try {
                responseObserver.onNext(numberNoteResp);
            } catch (Throwable throwable) {
                logger.error("NumbersStreamClientObserver::onNext(): exception[{}]", throwable.getMessage());
                return;
            }
            logger.info("NumbersStreamClientObserver::onNext(): REQ number[{}], RESP number[{}]",
                    numberNoteReq.getNumber(), numberNoteResp.getNumber());
        }

        @Override
        public void onError(Throwable throwable) {
            logger.error("NumbersStreamClientObserver::onError(): exception[{}]", throwable.getMessage());
        }

        @Override
        public void onCompleted() {
            responseObserver.onCompleted();
            logger.info("NumbersStreamClientObserver::onCompleted():");
        }
    }
}
