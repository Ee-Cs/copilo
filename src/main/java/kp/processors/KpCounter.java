package kp.processors;

import kp.utils.Utils;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Properties;
import java.util.function.Function;

import static kp.Constants.*;

/**
 * Counts the records in topics.
 * <p>
 * Uses the {@link StreamsBuilder} to define the actual processing topology.
 * </p>
 */
public class KpCounter {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final Properties properties;
    private final Topology topology;

    /**
     * Constructor.
     */
    public KpCounter() {

        this.properties = Utils.initializePropertiesForStream("kp-counter");
        this.topology = initializeTopology();
        Utils.describeTopology(topology.describe());
    }

    /**
     * Starts the {@link KafkaStreams}.
     */
    public void startStreams() {

        try (final KafkaStreams kafkaStreams = new KafkaStreams(topology, properties)) {
            kafkaStreams.start();
            logger.info("counter started");
            Utils.block();
        }
    }

    /**
     * Initializes the {@link Topology}.
     *
     * @return the {@link Topology}
     */
    private Topology initializeTopology() {

        final Function<String, ForeachAction<String, Long>> foreachActionFunction = topicName ->
                (key, value) -> logger.info("topic[{}], key[{}], count[{}]", topicName, key, value);
        final StreamsBuilder streamsBuilder = new StreamsBuilder();

        streamsBuilder.<String, String>stream(TOPIC_PROD_1).groupByKey().count().toStream()
                .foreach(foreachActionFunction.apply(TOPIC_PROD_1));
        streamsBuilder.<String, String>stream(TOPIC_PROD_2).groupByKey().count().toStream()
                .foreach(foreachActionFunction.apply(TOPIC_PROD_2));
        streamsBuilder.<String, String>stream(TOPIC_CONS_1).groupByKey().count().toStream()
                .foreach(foreachActionFunction.apply(TOPIC_CONS_1));
        streamsBuilder.<String, String>stream(TOPIC_CONS_2).groupByKey().count().toStream()
                .foreach(foreachActionFunction.apply(TOPIC_CONS_2));
        return streamsBuilder.build();
    }

}
