package kp.producer.controller;

import kp.common.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The producer controller.
 */
@RestController
public class ProducerController {
    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private static final List<String> TEXT_LIST = Arrays.stream("ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("")).toList();
    private static final AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * Produces the {@link Content}.
     *
     * @return the {@link Content}
     */
    @GetMapping("/content")
    public Content getContent() {

        final int index = atomicInteger.getAndIncrement() % TEXT_LIST.size();
        final Content content = new Content(TEXT_LIST.get(index));
        if (logger.isInfoEnabled()) {
            logger.info("getContent(): content text[{}]", content.text());
        }
        return content;
    }

}