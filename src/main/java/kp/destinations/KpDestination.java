package kp.destinations;

import kp.models.ApprovalStatus;
import kp.models.Information;
import kp.utils.Utils;
import org.apache.pulsar.client.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.atomic.AtomicBoolean;

import static kp.Constants.KP_KEY;
import static kp.Constants.TOPIC_SELECT_ORIG;

/**
 * The destination.
 * <p>
 * Receives the {@link Message}s with the {@link Information} and responds to the origin.
 * </p>
 */
public class KpDestination {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private final AtomicBoolean atomicBoolean = new AtomicBoolean();

    /**
     * Default constructor.
     */
    public KpDestination() {
        // constructor is empty
    }

    /**
     * Processes the {@link Message}s with the {@link Information}.
     *
     * @param consumerTopic the consumer topic
     */
    public void process(String consumerTopic) {

        try (PulsarClient pulsarClient = Utils.createPulsarClient();
             Consumer<Information> consumer = Utils.createConsumer(pulsarClient, consumerTopic);
             Producer<Information> producer = Utils.createProducer(pulsarClient, TOPIC_SELECT_ORIG)) {

            while (Utils.sleepMillis()) {
                receiveAndRespond(consumer, producer);
            }
        } catch (IOException e) {
            logger.error("process(): consumerTopic[{}], exception[{}]", consumerTopic, e.getMessage());
        }
    }

    /**
     * Receives messages at the destination and responds to the origin.
     * <p>
     * This follows the sequence:
     * </p>
     * <ol>
     * <li><i>Consume</i></li>
     * <li><i>Process</i></li>
     * <li><i>Produce</i></li>
     * </ol>
     *
     * @param consumer the {@link Consumer}
     * @param producer the {@link Producer}
     * @throws PulsarClientException if a Pulsar client error occurs
     */
    private void receiveAndRespond(Consumer<Information> consumer, Producer<Information> producer)
            throws PulsarClientException {

        // Consume
        final Message<Information> message = consumer.receive();
        try {
            final Information information = message.getValue();
            if (logger.isInfoEnabled()) {
                logger.info("""
                                receiveAndRespond():
                                \ttopic[{}],
                                \tkey[{}], sequenceId[{}], messageId[{}], information id[{}]""",
                        message.getTopicName(), message.getKey(), message.getSequenceId(),
                        message.getMessageId(), information.getId());
            }
            // Process
            information.setApprovalStatus(atomicBoolean.get() ? ApprovalStatus.APPROVED : ApprovalStatus.REJECTED);
            atomicBoolean.set(!atomicBoolean.get());
            // Produce
            producer.newMessage().key(KP_KEY).value(information).sendAsync()
                    .thenAccept(this::sendOperationCompleted);
            consumer.acknowledge(message);
        } catch (Exception e) {
            logger.error("receiveAndRespond(): exception[{}]", e.getMessage());
            consumer.negativeAcknowledge(message);
            // by default, failed messages are replayed after a 1-minute delay
        }
    }

    /**
     * Tracks the completion of the send operation.
     *
     * @param messageId the {@link MessageId} assigned by the broker to the published message
     */
    private void sendOperationCompleted(MessageId messageId) {
        logger.debug("sendOperationCompleted(): messageId[{}]", messageId);
    }

}
