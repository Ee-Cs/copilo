package kp.services.clients;

import io.grpc.stub.StreamObserver;
import kp.proto.NumberNote;
import kp.proto.NumbersChatServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class AaaService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private final NumbersChatServiceGrpc.NumbersChatServiceBlockingStub numbersChatServiceBlockingStub;
    private final NumbersChatServiceGrpc.NumbersChatServiceStub numbersChatServiceStub;

    public AaaService(@Autowired NumbersChatServiceGrpc.NumbersChatServiceBlockingStub numbersChatServiceBlockingStub,
                      @Autowired NumbersChatServiceGrpc.NumbersChatServiceStub numbersChatServiceStub) {
        this.numbersChatServiceBlockingStub = numbersChatServiceBlockingStub;
        this.numbersChatServiceStub = numbersChatServiceStub;
    }

    public void startSingleReply() {

        logger.info("\n" + "#".repeat(150) + " SINGLE REPLY");
        int number = 1;
        for (int index = 0; index < 3; index++) {
            final NumberNote numberNoteReq = NumberNote.newBuilder().setNumber(number).build();
            final NumberNote numberNoteResp = numbersChatServiceBlockingStub.numbersSingleReply(numberNoteReq);
            logger.info("startSingleReply(): index[{}], REQ number[{}], RESP number[{}]",
                    index, numberNoteReq.getNumber(), numberNoteResp.getNumber());
            number = numberNoteResp.getNumber() + 1;
        }
    }

    public void startStreamServer() {
        logger.info("\n" + "#".repeat(150) + " STREAM SERVER");
        final int number = 11;
        final NumberNote numberNoteReq = NumberNote.newBuilder().setNumber(number).build();
        logger.info("startStreamServer(): REQ number[{}]", numberNoteReq.getNumber());
        numbersChatServiceBlockingStub.numbersStreamServerReply(numberNoteReq)
                .forEachRemaining(numberNoteResp ->
                        logger.info("startStreamServer(): RESP number[{}]", numberNoteResp.getNumber())
                );
    }

    public void startStreamClient() {
        logger.info("\n" + "#".repeat(150) + " STREAM CLIENT");
        final StreamObserver<NumberNote> requestObserver =
                numbersChatServiceStub.numbersStreamClientReply(new ResponseObserver());
        int number = 101;
        for (int index = 0; index < 2; index++) {
            final NumberNote numberNoteReq = NumberNote.newBuilder().setNumber(number).build();
            logger.info("startStreamClient(): REQ number[{}]", numberNoteReq.getNumber());
            requestObserver.onNext(numberNoteReq);
            number = number + 1;
        }
        requestObserver.onCompleted();
        logger.info("startStreamClient(): completed");
    }

    private record ResponseObserver() implements StreamObserver<NumberNote> {
        @Override
        public void onNext(NumberNote numberNoteResp) {
            logger.info("ResponseObserver::onNext(): RESP number[{}]", numberNoteResp.getNumber());
        }

        @Override
        public void onError(Throwable throwable) {
            logger.error("ResponseObserver::onError(): exception[{}]", throwable.getMessage());
        }

        @Override
        public void onCompleted() {
            logger.info("ResponseObserver::onCompleted():");
        }
    }

}
//try {Thread.sleep(5000);} catch(Throwable e) {}//FIXME

