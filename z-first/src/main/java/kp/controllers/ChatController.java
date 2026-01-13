package kp.controllers;

import kp.services.clients.AaaService;
import kp.services.clients.NumbersChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping("/chats")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private final NumbersChatService numbersChatService;
    private final AaaService aaaService;

    public ChatController(NumbersChatService numbersChatService, AaaService aaaService) {
        this.numbersChatService = numbersChatService;
        this.aaaService = aaaService;
    }

    @GetMapping("/numbers/{limit}")
    public String startNumbersChat(@PathVariable("limit") int limit) {

        final boolean result = numbersChatService.startNumbersChat(limit);
        logger.info("startNumbersChat(): result[{}]", result);
        return result ? "NUMBERS CHAT OK" : "NUMBERS CHAT ERROR";
    }

    @GetMapping("/aaa")
    public String startAaaService() {
        if(!true) {
            aaaService.startSingleReply();
            aaaService.startStreamServer();
        }
        aaaService.startStreamClient();
        logger.debug("startAaaService():");
        return "OK";
    }
}
