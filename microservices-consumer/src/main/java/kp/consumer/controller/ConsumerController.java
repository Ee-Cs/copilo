package kp.consumer.controller;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kp.common.Content;
import kp.consumer.service.ConsumerService;

/**
 * The consumer controller.
 * 
 */
@RestController
public class ConsumerController {
	private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

	/**
	 * The {@link ConsumerService}.
	 */
	protected ConsumerService consumerService;

	/**
	 * Constructor.
	 * 
	 * @param consumerService the {@link ConsumerService}
	 */
	public ConsumerController(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	/**
	 * Launches the {@link ConsumerService}.
	 * 
	 * @return the {@link Content}
	 */
	@GetMapping("/content")
	public Content getContent() {

		final Content content = consumerService.getContent();
		if (logger.isInfoEnabled()) {
			logger.info("getContent(): content text[{}]", content.text());
		}
		return content;
	}

}