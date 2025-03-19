package kp.selectors;

import kp.models.Information;
import kp.utils.Utils;
import org.apache.pulsar.client.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import static kp.Constants.KP_KEY;

/**
 * The selector.
 * <p>
 * Selects the target for the {@link Message}s with the {@link Information}.
 * </p>
 * <ul>
 * <li>The first instance receives the request messages from the origin and sends them to the selected destination.</li>
 * <li>The second instance receives the response messages from the destination and sends them to the selected origin.</li>
 * </ul>
 */
public class KpSelector {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    /**
     * Default constructor.
     */
    public KpSelector() {
        // constructor is empty
    }

    /**
     * Processes the {@link Message}s with the {@link Information}.
     *
     * @param consumerTopic  the consumer topic
     * @param producerTopic1 the producer topic 1
     * @param producerTopic2 the producer topic 2
     */
    public void process(String consumerTopic, String producerTopic1, String producerTopic2) {

        try (final PulsarClient pulsarClient = Utils.createPulsarClient();
             final Consumer<Information> consumer = Utils.createConsumerWithFailover(pulsarClient, consumerTopic);
             final Producer<Information> producer1 = Utils.createProducer(pulsarClient, producerTopic1);
             final Producer<Information> producer2 = Utils.createProducer(pulsarClient, producerTopic2)) {

            while (Utils.sleepMillis()) {
                select(consumer, producer1, producer2);
            }
        } catch (IOException e) {
            logger.error("process(): consumerTopic[{}], producerTopic1[{}], producerTopic2[{}], exception[{}]",
                    consumerTopic, producerTopic1, producerTopic2, e.getMessage());
        }
    }

    /**
     * Selects the producer and produces the message.
     *
     * @param consumer  the {@link Consumer}
     * @param producer1 the 1st {@link Producer}
     * @param producer2 the 2nd {@link Producer}
     * @throws PulsarClientException the {@link PulsarClientException}
     */
    private void select(Consumer<Information> consumer, Producer<Information> producer1,
                        Producer<Information> producer2) throws PulsarClientException {

        final Message<Information> message = consumer.receive();
        try {
            final Information information = message.getValue();
            final boolean flag = information.getId() % 2 == 1;
            if (logger.isInfoEnabled()) {
                logger.info("""
                                select():
                                \ttopic[{}],
                                \tkey[{}], sequence id[{}], message id[{}], information id[{}], target[{}]""",
                        message.getTopicName(), message.getKey(), message.getSequenceId(), message.getMessageId(),
                        information.getId(), flag ? "one" : "two");
            }
            final Producer<Information> producer = flag ? producer1 : producer2;
            producer.newMessage().key(KP_KEY).value(information).sendAsync().thenAccept(this::sendOperationCompleted);
            consumer.acknowledge(message);
        } catch (Exception e) {
            logger.error("select(): exception[{}]", e.getMessage());
            consumer.negativeAcknowledge(message);
            // by default, the failed messages are replayed after a 1-minute delay
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
