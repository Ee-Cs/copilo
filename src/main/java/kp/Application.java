package kp;

import kp.destinations.KpDestination;
import kp.originators.KpOriginator;
import kp.reporters.KpReporter;
import kp.selectors.KpSelector;
import kp.utils.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import static kp.Constants.*;

/**
 * The main class for the research of the Pulsar broker.
 * <p>
 * In the <b>publish/subscribe</b> design pattern, message publishers don’t
 * deliver messages to specific subscribers. Instead, message consumers
 * subscribe to topics of interest.
 * </p>
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

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

        final String selection = Optional.ofNullable(args)
                .filter(arg -> arg.length > 0).map(arg -> arg[0]).orElse("");
        switch (selection) {
            case "originator1":
                new KpOriginator().process(TOPIC_ORIG_1, Marker.ODD);
                break;
            case "originator2":
                new KpOriginator().process(TOPIC_ORIG_2, Marker.EVEN);
                break;
            case "selectorDest":
                new KpSelector().process(TOPIC_SELECT_DEST, TOPIC_DEST_1, TOPIC_DEST_2);
                break;
            case "selectorOrig":
                new KpSelector().process(TOPIC_SELECT_ORIG, TOPIC_ORIG_1, TOPIC_ORIG_2);
                break;
            case "destination1":
                new KpDestination().process(TOPIC_DEST_1);
                break;
            case "destination2":
                new KpDestination().process(TOPIC_DEST_2);
                break;
            case "reporter":
                new KpReporter().process();
                break;
            default:
                logger.error("unknown application function");
                break;
        }
    }
}