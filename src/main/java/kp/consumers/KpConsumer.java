package kp.consumers;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static kp.Constants.*;

/**
 * The consumer.
 * <p>
 * Consumes the {@link ConsumerRecord}s with the {@link KafkaConsumer}.
 * </p>
 */
public class KpConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    final Properties properties;
    final String topic;

    /**
     * Constructor.
     *
     * @param topic the topic
     */
    public KpConsumer(String topic) {

        this.properties = initProperties();
        this.topic = topic;
    }

    /**
     * Consumes the {@link ConsumerRecord}s.
     */
    public void consumeRecords() {

        try (final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(Collections.singletonList(topic));
            logger.info("consumer started, topic[{}]", topic);

            while (pauseConsuming()) {
                final ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
                if (consumerRecords.isEmpty()) {
                    continue;
                }
                showRecords(consumerRecords);
            }
        }
    }

    /**
     * Initializes the {@link Properties}.
     *
     * @return the {@link Properties}
     */
    private Properties initProperties() {

        final Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        props.setProperty(CommonClientConfigs.GROUP_ID_CONFIG, "kp-consumer-group");
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        return props;
    }

    /**
     * Shows the {@link ConsumerRecords} content.
     *
     * @param consumerRecords the {@link ConsumerRecords}
     */
    private void showRecords(ConsumerRecords<String, String> consumerRecords) {

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LINE_SEP).append(THICK_LINE).append(LINE_SEP);
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            stringBuilder.append("ConsumerRecord: offset[%d], key[%s], value[%s]%n".formatted(
                    consumerRecord.offset(), consumerRecord.key(), consumerRecord.value()));
        }
        stringBuilder.append(THICK_LINE);
        final String message = stringBuilder.toString();
        logger.info(message);
    }

    /**
     * Pauses for the given milliseconds.
     * <p>
     * Method created only for defining an exit from an endless loop, which is not used here.
     * </p>
     * @return the result flag
     */
    private boolean pauseConsuming() {

        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            logger.error("pauseConsuming(): exception[{}]", e.getMessage());
            return false;
        }
        return true;
    }

}
