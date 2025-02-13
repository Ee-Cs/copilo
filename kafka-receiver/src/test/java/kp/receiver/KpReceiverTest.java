package kp.receiver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;

import kp.model.Box;

import java.lang.invoke.MethodHandles;

/**
 * The receiver test.
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ImportAutoConfiguration(value = TestChannelBinderConfiguration.class,
		exclude = { KafkaAutoConfiguration.class })
@DirtiesContext
class KpReceiverTest {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

	@Autowired
	private InputDestination inputDestination;

	private static final String INPUT_DESTINATION_NAME = "kp-2";
	private static final String RECEIVED_PAYLOAD = """
			{
			"sndNum" : 12345,
			"prcNum" : 67890,
			"recNum" : 0,
			"milliseconds" : 0
			}
			""";
	private static final int EXPECTED_SND_NUM = 12345;
	private static final int EXPECTED_PRC_NUM = 67890;
	private static final int EXPECTED_REC_NUM = 1;

	private static final String BOX_NULL_ERR_MSG = "Box is null";
	private static final String SND_NUM_ERR_MSG = "Bad send number in Box";
	private static final String PRC_NUM_ERR_MSG = "Bad process number in Box";
	private static final String REC_NUM_ERR_MSG = "Bad receive number in Box";
	private static final String MILLIS_ERR_MSG = "Bad millis in Box";

	/**
	 * Should receive the {@link Box}.
	 * 
	 */
	@Test
	void shouldReceiveBox() {
		// GIVEN
		final Message<String> inputMessage = MessageBuilder.withPayload(RECEIVED_PAYLOAD).build();
		// WHEN
		inputDestination.send(inputMessage, INPUT_DESTINATION_NAME);
		// THEN
		final Box receivedBox = KpReceiver.getReceivedBox();
		Assertions.assertNotNull(receivedBox, BOX_NULL_ERR_MSG);
		Assertions.assertEquals(EXPECTED_SND_NUM, receivedBox.getSndNum(), SND_NUM_ERR_MSG);
		Assertions.assertEquals(EXPECTED_PRC_NUM, receivedBox.getPrcNum(), PRC_NUM_ERR_MSG);
		Assertions.assertEquals(EXPECTED_REC_NUM, receivedBox.getRecNum(), REC_NUM_ERR_MSG);
		Assertions.assertTrue(receivedBox.getMilliseconds() > 0, MILLIS_ERR_MSG);
		logger.info("shouldReceiveBox():");
	}
}
