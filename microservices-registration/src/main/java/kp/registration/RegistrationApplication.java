package kp.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * This application runs the <b>Eureka</b> registration server.
 */
@SpringBootApplication
@EnableEurekaServer
public class RegistrationApplication {

    /**
     * The primary entry point for launching the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(RegistrationApplication.class, args);
    }
}