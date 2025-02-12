package kp.processors;

import kp.utils.Utils;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Properties;

import static kp.Constants.*;

/**
 * Compares the records in producer topics with the records in consumer topics:
 * <ul>
 * <li>'<b>prod-1</b>' versus '<b>cons-1</b>'
 * <li>'<b>prod-2</b>' versus '<b>cons-2</b>'
 * </ul>
 * <p>
 * Uses the {@link StreamsBuilder} to define the actual processing topology.
 * </p>
 */
public class KpComparer {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final Properties properties;
    private final Topology topology;
    private static final String TOPIC_VAL_FMT = "topic/value[%s]/[%s], topic/value[%s]/[%s]";
    private static final String KEY_VAL_FMT = "key[{}], {}";

    /**
     * Constructor.
     */
    public KpComparer() {

        this.properties = Utils.initializePropertiesForStream("kp-comparer");
        this.topology = initializeTopology();
        Utils.describeTopology(topology.describe());
    }

    /**
     * Starts the {@link KafkaStreams}.
     */
    public void startStreams() {

        try (final KafkaStreams kafkaStreams = new KafkaStreams(topology, properties)) {
            kafkaStreams.start();
            logger.info("comparer started");
            Utils.block();
        }
    }

    /**
     * Initializes the {@link Topology}.
     *
     * @return the {@link Topology}
     */
    private Topology initializeTopology() {

        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        final KStream<String, String> joinedStream1 = streamsBuilder.<String, String>stream(TOPIC_PROD_1).leftJoin(
                streamsBuilder.globalTable(TOPIC_CONS_1),
                (inputKey, _) -> inputKey,
                (inputValue, globalValue) -> TOPIC_VAL_FMT.formatted(
                        TOPIC_PROD_1, inputValue, TOPIC_CONS_1, globalValue));
        joinedStream1.foreach((key, value) -> {
            if (KEY_LIST_1.getFirst().equals(key)) {
                logger.info(KEY_VAL_FMT, key, value);
            }
        });
        final KStream<String, String> joinedStream2 = streamsBuilder.<String, String>stream(TOPIC_PROD_2).leftJoin(
                streamsBuilder.globalTable(TOPIC_CONS_2),
                (inputKey, _) -> inputKey,
                (inputValue, globalValue) -> TOPIC_VAL_FMT.formatted(
                        TOPIC_PROD_2, inputValue, TOPIC_CONS_2, globalValue));
        joinedStream2.foreach((key, value) -> {
            if (KEY_LIST_2.getLast().equals(key)) {
                logger.info(KEY_VAL_FMT, key, value);
            }
        });
        return streamsBuilder.build();
    }

}
