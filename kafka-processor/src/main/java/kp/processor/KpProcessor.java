package kp.processor;

import kp.model.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * The processor using the {@link Function}.
 */
@Configuration
public class KpProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private static final AtomicInteger atomic = new AtomicInteger();
    private static final String NUM_FMT = "%03d";

    /**
     * Creates the {@link Function} bean for processing the {@link Box}.
     *
     * @return the {@link Function}
     */
    @Bean
    public Function<Box, Box> processBox() {
        return box -> {
            box.setPrcNum(atomic.incrementAndGet());
            final long elapsed = Instant.now().toEpochMilli() - box.getMilliseconds();
            box.setMilliseconds(Instant.now().toEpochMilli());
            if (logger.isInfoEnabled()) {
                logger.info("Snd[{}], Prc[{}], elapsed time[{}]",
                        NUM_FMT.formatted(box.getSndNum()), NUM_FMT.formatted(box.getPrcNum()), elapsed);
            }
            return box;
        };
    }

}