package kp;

import kp.consumers.KpConsumer;
import kp.processors.KpComparer;
import kp.processors.KpCounter;
import kp.processors.KpTransformer;
import kp.producers.KpProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * The main class for Kafka broker on Docker research.
 * <p>
 * Used libraries:
 * </p>
 * <ol>
 * <li>KafkaProducer</li>
 * <li>KafkaStreams</li>
 * <li>KafkaConsumer</li>
 * </ol>
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private static final int WAIT_FOR_KAFKA_SECONDS = 15;

    /**
     * Private constructor to prevent instantiation.
     */
    private Application() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The primary entry point for launching the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        waitForFullyOperationalKafkaService();

        final String selection = Optional.ofNullable(args).map(Arrays::asList).filter(Predicate.not(List::isEmpty))
                .map(List::getFirst).orElse("");
        switch (selection) {
            case "consumer1" -> new KpConsumer(Constants.TOPIC_CONS_1).consumeRecords();
            case "consumer2" -> new KpConsumer(Constants.TOPIC_CONS_2).consumeRecords();
            case "transformer" -> new KpTransformer().startStreams();
            case "counter" -> new KpCounter().startStreams();
            case "comparer" -> new KpComparer().startStreams();
            case "producer" -> new KpProducer().produceRecords();
            default -> logger.warn("Unknown application function");
        }
    }

    /**
     * Waits for the Kafka service to be fully operational.
     */
    private static void waitForFullyOperationalKafkaService() {

        try {
            TimeUnit.SECONDS.sleep(WAIT_FOR_KAFKA_SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            logger.error("waitForFullyOperationalKafkaService(): InterruptedException[{}]", e.getMessage());
        }
    }

}
