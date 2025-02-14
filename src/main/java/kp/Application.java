package kp;

import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The SpringBoot application with enabled Zeebe client.
 * <p>
 * The process solution with included process definitions:
 * </p>
 * <ul>
 *     <li>BPMN diagram</li>
 *     <li>DRD diagram</li>
 * </ul>
 */
@SpringBootApplication
@Deployment(resources = Constants.BUSINESS_PROCESS_DIAGRAM)
public class Application {

    /**
     * The primary entry point for launching the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}