package kp.consumer;

import kp.consumer.controller.ConsumerController;
import kp.consumer.service.ConsumerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * The consumer web application.
 * <p>
 * This is a microservice <b>producer-service</b> client.
 * </p>
 * <p>
 * It uses the <b>Eureka</b> Discovery Server to find the producer microservice.
 * </p>
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(useDefaultFilters = false) // disable component scanner
public class ConsumerApplication {

    private static final String CIRCUIT_BREAKER_ID = "content";

    /**
     * The primary entry point for launching the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    /**
     * The {@link RestTemplate} configured to use a 'LoadBalancerClient'.
     *
     * @return the {@link RestTemplate}
     */
    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Creates the {@link ConsumerController}.
     *
     * @param circuitBreakerFactory the {@link Resilience4JCircuitBreakerFactory}
     * @return the {@link ConsumerController}
     */
    @Bean
    public ConsumerController consumerController(Resilience4JCircuitBreakerFactory circuitBreakerFactory) {
        return new ConsumerController(consumerService(circuitBreakerFactory));
    }

    /**
     * Creates the {@link ConsumerService}.
     *
     * @param circuitBreakerFactory the {@link Resilience4JCircuitBreakerFactory}
     * @return the {@link ConsumerService}
     */
    @Bean
    public ConsumerService consumerService(Resilience4JCircuitBreakerFactory circuitBreakerFactory) {
        return new ConsumerService(circuitBreakerFactory.create(CIRCUIT_BREAKER_ID), restTemplate());
    }

}