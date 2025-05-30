package kp.controllers;

import kp.services.clients.NumbersChatService;
import kp.services.clients.WordsChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

/**
 * REST controller for chat-related operations.
 * <p>
 * Provides endpoints to start chat processes based on words or numbers.
 * Delegates actual chat execution to service classes, which use gRPC clients.
 * </p>
 */
@RestController
@RequestMapping("/chats")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private final WordsChatService wordsChatService;
    private final NumbersChatService numbersChatService;

    /**
     * Parameterized constructor.
     *
     * @param wordsChatService   the service handling word-based chats
     * @param numbersChatService the service handling number-based chats
     */
    public ChatController(WordsChatService wordsChatService, NumbersChatService numbersChatService) {
        this.wordsChatService = wordsChatService;
        this.numbersChatService = numbersChatService;
    }

    /**
     * Starts the words chat process.
     *
     * @param limit the upper limit for the words chat
     * @return a result message indicating the success or failure of the operation
     */
    @GetMapping("/words/{limit}")
    public String startWordsChat(@PathVariable("limit") int limit) {

        final boolean result = wordsChatService.startWordsChat(limit);
        logger.info("startWordsChat(): result[{}]", result);
        return result ? "WORDS CHAT OK" : "WORDS CHAT ERROR";
    }

    /**
     * Starts the numbers chat process.
     *
     * @param limit the upper limit for the numbers chat
     * @return a result message indicating the success or failure of the operation
     */
    @GetMapping("/numbers/{limit}")
    public String startNumbersChat(@PathVariable("limit") int limit) {

        final boolean result = numbersChatService.startNumbersChat(limit);
        logger.info("startNumbersChat(): result[{}]", result);
        return result ? "NUMBERS CHAT OK" : "NUMBERS CHAT ERROR";
    }
}
