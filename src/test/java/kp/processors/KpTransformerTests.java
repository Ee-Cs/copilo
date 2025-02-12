package kp.processors;

import kp.utils.Utils;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.stream.IntStream;

import static kp.Constants.*;

/**
 * Testing the {@link Topology} from {@link KpTransformer} with {@link TopologyTestDriver}.
 */
public class KpTransformerTests {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private static TopologyTestDriver testDriver;

    /**
     * Set up before each test.
     */
    @BeforeAll
    static void setup() {
        testDriver = new TopologyTestDriver(new KpTransformer().getTopology(),
                Utils.initializePropertiesForStream("kp-test"));
    }

    /**
     * Tear down after each test.
     */
    @AfterAll
    static void tearDown() {
        testDriver.close();
    }

    /**
     * Should direct records to the consumer of the 1st topic.
     */
    @Test
    public void shouldSplitToBranchOne() {

        try (Serde<String> serde = Serdes.String()) {
            // GIVEN
            final TestInputTopic<String, String> inputTopic = testDriver.createInputTopic(TOPIC_PROD_1,
                    serde.serializer(), serde.serializer());
            final TestOutputTopic<String, String> outputTopic = testDriver.createOutputTopic(TOPIC_CONS_1,
                    serde.deserializer(), serde.deserializer());
            // WHEN
            IntStream.range(0, KEY_LIST_1.size()).boxed()
                    .forEach(i -> inputTopic.pipeInput(KEY_LIST_1.get(i), VAL_LIST_P1.get(i)));
            final Map<String, String> actualMap = outputTopic.readKeyValuesToMap();
            // THEN
            Assertions.assertTrue(outputTopic.isEmpty());
            Assertions.assertEquals(actualMap.get(KEY_LIST_1.get(0)), VAL_LIST_C1.get(0));
            Assertions.assertNull(actualMap.get(KEY_LIST_1.get(1)));
            Assertions.assertEquals(actualMap.get(KEY_LIST_1.get(2)), VAL_LIST_C1.get(1));
            Assertions.assertNull(actualMap.get(KEY_LIST_1.get(3)));
        }
        logger.info("shouldSplitToBranchOne():");
    }

    /**
     * Should direct records to the consumer of the 2nd topic.
     */
    @Test
    public void shouldSplitToBranchTwo() {

        try (Serde<String> serde = Serdes.String()) {
            // GIVEN
            final TestInputTopic<String, String> inputTopic = testDriver.createInputTopic(TOPIC_PROD_1,
                    serde.serializer(), serde.serializer());
            final TestOutputTopic<String, String> outputTopic = testDriver.createOutputTopic(TOPIC_CONS_2,
                    serde.deserializer(), serde.deserializer());
            // WHEN
            IntStream.range(0, KEY_LIST_1.size()).boxed()
                    .forEach(i -> inputTopic.pipeInput(KEY_LIST_1.get(i), VAL_LIST_P1.get(i)));
            final Map<String, String> actualMap = outputTopic.readKeyValuesToMap();
            // THEN
            Assertions.assertTrue(outputTopic.isEmpty());
            Assertions.assertNull(actualMap.get(KEY_LIST_1.get(0)));
            Assertions.assertEquals(actualMap.get(KEY_LIST_1.get(1)), VAL_LIST_C2.get(0));
            Assertions.assertNull(actualMap.get(KEY_LIST_1.get(2)));
            Assertions.assertEquals(actualMap.get(KEY_LIST_1.get(3)), VAL_LIST_C2.get(1));
        }
        logger.info("shouldSplitToBranchTwo():");
    }

}
