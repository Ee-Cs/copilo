package kp.processors;

import kp.utils.Utils;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Properties;

import static kp.Constants.*;

/**
 * Directs the records from the producer topics to the consumer topics.
 * <p>
 * Uses the {@link StreamsBuilder} to define the actual processing topology.
 * </p>
 * <p>
 * The {@link KafkaStreams} DSL (Domain Specific Language) is built on top of the Streams Processor API.
 * </p>
 */
public class KpTransformer {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final Properties properties;
    private final Topology topology;

    /**
     * Constructor.
     */
    public KpTransformer() {

        this.properties = Utils.initializePropertiesForStream("kp-transformer");
        this.topology = initializeTopology();
        Utils.describeTopology(topology.describe());
    }

    /**
     * Starts the {@link KafkaStreams}.
     */
    public void startStreams() {

        try (final KafkaStreams kafkaStreams = new KafkaStreams(topology, properties)) {
            kafkaStreams.start();
            logger.info("transformer started");
            Utils.block();
        }
    }

    /**
     * Gets the {@link Topology}. Method used for tests.
     *
     * @return the {@link Topology}
     */
    public Topology getTopology() {
        return topology;
    }

    /**
     * Initializes the {@link Topology}.
     *
     * @return the {@link Topology}
     */
    private Topology initializeTopology() {

        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        final KStream<String, String> inputStream = streamsBuilder.stream(List.of(TOPIC_PROD_1, TOPIC_PROD_2));

        final Predicate<String, String> predicate1 = (_, value) -> VAL_LIST_C1
                .contains(value.substring(0, 1).toLowerCase());
        final Predicate<String, String> predicate2 = (_, value) -> VAL_LIST_C2
                .contains(value.substring(0, 1).toLowerCase());
        /*
         * Drops all non-matching records because there is no default branch.
         */
        inputStream.split()
                .branch(predicate1, Branched.withConsumer(stream -> stream.to(TOPIC_CONS_1)))
                .branch(predicate2, Branched.withConsumer(stream -> stream.to(TOPIC_CONS_2)));
        return streamsBuilder.build();
    }

}