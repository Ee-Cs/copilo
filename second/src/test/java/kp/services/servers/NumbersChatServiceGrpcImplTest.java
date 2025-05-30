package kp.services.servers;

import io.grpc.internal.testing.StreamRecorder;
import io.grpc.stub.StreamObserver;
import kp.proto.NumberNote;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The tests for the chat with numbers.
 */
@SpringBootTest
class NumbersChatServiceGrpcImplTest {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private final NumbersChatServiceGrpcImpl numbersChatServiceGrpc;
    // the last test number causes expected integer  overflow
    private static final List<Integer> REQUEST_NUMBERS = List.of(Integer.MIN_VALUE, 0, 10, Integer.MAX_VALUE);
    private static final List<Integer> RESPONSE_NUMBERS = List.of(Integer.MIN_VALUE + 1, 1, 11, Integer.MIN_VALUE);

    /**
     * Parameterized constructor.
     *
     * @param numbersChatServiceGrpc the {@link NumbersChatServiceGrpcImpl}
     */
    NumbersChatServiceGrpcImplTest(@Autowired NumbersChatServiceGrpcImpl numbersChatServiceGrpc) {
        this.numbersChatServiceGrpc = numbersChatServiceGrpc;
    }

    /**
     * Verifies chatting with numbers.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("🟩 should chat with numbers")
    void shouldChatWithNumbers() throws Exception {
        // GIVEN
        final StreamRecorder<NumberNote> streamRecorder = StreamRecorder.create();
        // WHEN
        final StreamObserver<NumberNote> streamObserver = numbersChatServiceGrpc.numbersChat(streamRecorder);
        REQUEST_NUMBERS.forEach(number -> streamObserver.onNext(NumberNote.newBuilder().setNumber(number).build()));
        streamObserver.onCompleted();
        // THEN
        if (!streamRecorder.awaitCompletion(5, TimeUnit.SECONDS)) {
            Fail.fail("The call did not terminate in time");
        }
        Assertions.assertNull(streamRecorder.getError(), "the stream terminating error");
        final List<NumberNote> results = streamRecorder.getValues();
        Assertions.assertEquals(RESPONSE_NUMBERS.size(), results.size(), "number of received values");
        for (int i = 0; i < results.size(); i++) {
            Assertions.assertEquals(RESPONSE_NUMBERS.get(i), results.get(i).getNumber(),
                    "response number from chat");
        }
        logger.info("shouldChatWithNumbers():");
    }
}
