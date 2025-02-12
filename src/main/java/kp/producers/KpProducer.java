package kp.producers;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import static kp.Constants.*;

/**
 * The producer.
 * <p>
 * Sends the {@link ProducerRecord}s with the {@link KafkaProducer}.
 * </p>
 */
public class KpProducer {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final Properties properties;
    private static final AtomicInteger atomic = new AtomicInteger();
    private static final int PRODUCER_PAUSE_SECONDS = 15;

    /**
     * Constructor.
     */
    public KpProducer() {

        this.properties = initProperties();
    }

    /**
     * Produces the {@link ProducerRecord}s.
     */
    public void produceRecords() {

        boolean executeFlag = true;
        while (executeFlag) {
            produceRecordsSet();
            executeFlag = pauseProducing();
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
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        props.put(ProducerConfig.ACKS_CONFIG, "all");
        return props;
    }

    /**
     * Produces the set of {@link ProducerRecord}s.
     */
    private void produceRecordsSet() {

        final int counter = atomic.incrementAndGet();
        final StringBuilder strBld = new StringBuilder();
        final BiConsumer<Producer<String, String>, ProducerRecord<String, String>> sendAction =
                (producer, rec) -> {
                    producer.send(rec);
                    strBld.append("ProducerRecord: key[%s], value[%s]%n".formatted(rec.key(), rec.value()));
                };
        strBld.append(LINE_SEP).append(THICK_LINE).append(LINE_SEP);
        try (final Producer<String, String> producer = new KafkaProducer<>(properties)) {
            IntStream.range(0, KEY_LIST_1.size()).boxed()
                    .map(i -> new ProducerRecord<>(TOPIC_PROD_1, KEY_LIST_1.get(i),
                            "%s-%02d".formatted(VAL_LIST_P1.get(i), counter)))
                    .forEach(rec -> sendAction.accept(producer, rec));
        }
        strBld.append(THICK_LINE).append(LINE_SEP);

        strBld.append(THICK_LINE).append(LINE_SEP);
        try (final Producer<String, String> producer = new KafkaProducer<>(properties)) {
            IntStream.range(0, KEY_LIST_2.size()).boxed()
                    .map(i -> new ProducerRecord<>(TOPIC_PROD_2, KEY_LIST_2.get(i),
                            "%s-%02d".formatted(VAL_LIST_P2.get(i), counter)))
                    .forEach(rec -> sendAction.accept(producer, rec));
        }
        strBld.append(THICK_LINE);
        logger.info(strBld.toString());
    }

    /**
     * Pauses the record producing.
     *
     * @return the flag
     */
    private boolean pauseProducing() {

        try {
            TimeUnit.SECONDS.sleep(PRODUCER_PAUSE_SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            logger.error("pauseProducing(): exception[{}]", e.getMessage());
            return false;
        }
        return true;
    }

}
