package kp.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The producer application.
 * <p>
 * The microservice <b>producer-service</b> is registered
 * with the <b>Eureka</b> Service Discovery Server.
 * </p>
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ProducerApplication {

    /**
     * The primary entry point for launching the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }
}
