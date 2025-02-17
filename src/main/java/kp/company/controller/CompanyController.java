package kp.company.controller;

import java.lang.invoke.MethodHandles;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The web controller for the company.
 * 
 */
@Controller
public class CompanyController {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Gets the company.
	 * 
	 * @return the view name
	 */
	@GetMapping("/company")
	public String company() {
		logger.info("company():");
		return "company/home";
	}
}