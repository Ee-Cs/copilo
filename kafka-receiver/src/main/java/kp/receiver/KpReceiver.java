package kp.receiver;

import kp.model.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * The receiver using the {@link Consumer}.
 */
@Configuration
public class KpReceiver {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private static final AtomicInteger atomic = new AtomicInteger();
    private static final String NUM_FMT = "%03d";
    private static Box receivedBox;

    /**
     * Creates the {@link Consumer} bean for receiving the {@link Box}.
     *
     * @return the {@link Consumer}
     */
    @Bean
    public Consumer<Box> receiveBox() {
        return box -> {
            box.setRecNum(atomic.incrementAndGet());
            final long elapsed = Instant.now().toEpochMilli() - box.getMilliseconds();
            box.setMilliseconds(Instant.now().toEpochMilli());
            KpReceiver.setReceivedBox(box);
            if(logger.isInfoEnabled()) {
                logger.info("Snd[{}], Prc[{}], Rec[{}], elapsed time[{}]", NUM_FMT.formatted(box.getSndNum()),
                        NUM_FMT.formatted(box.getPrcNum()), NUM_FMT.formatted(box.getRecNum()), elapsed);
            }
        };
    }

    /**
     * Gets the received {@link Box}. This method is used for tests.
     *
     * @return the received {@link Box}
     */
    public static Box getReceivedBox() {
        return KpReceiver.receivedBox;
    }

    /**
     * Sets the received {@link Box}. This method is used for tests.
     *
     * @param receivedBox the received {@link Box} to set
     */
    public static void setReceivedBox(Box receivedBox) {
        KpReceiver.receivedBox = receivedBox;
    }

}
