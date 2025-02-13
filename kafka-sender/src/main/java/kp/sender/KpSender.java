package kp.sender;

import kp.model.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * The sender using the {@link Supplier}.
 */
@Configuration
public class KpSender {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private static final AtomicInteger atomic = new AtomicInteger();
    private static final String NUM_FMT = "%03d";

    /**
     * Creates the {@link Supplier} bean for sending the {@link Box}.
     *
     * @return the {@link Supplier}
     */
    @Bean
    public Supplier<Box> sendBox() {
        return () -> {
            final int sndNumber = atomic.incrementAndGet();
            final Box box = new Box(sndNumber, Instant.now().toEpochMilli());
            if (logger.isInfoEnabled()) {
                logger.info("Snd[{}]", NUM_FMT.formatted(sndNumber));
            }
            return box;
        };
    }

}