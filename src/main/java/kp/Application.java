package kp;

import kp.service.KpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * The application for the elastic search client.
 * <p>
 * <a href=
 * "https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/index.html">Elasticsearch
 * Java API Client</a>
 * </p>
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    /**
     * The primary entry point for launching the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        final String password = Optional.ofNullable(args).filter(arg -> arg.length > 0)
                .map(arg -> arg[0]).orElse("");
        if (password.isBlank()) {
            logger.error("main(): password is required!");
            System.exit(1);
        }
        final KpService kpService = new KpService(password);
        try {
            kpService.recreateIndex();
            kpService.addBoxesFromFile();
            waitAfterDatasetImport();
            kpService.searchWithQuery();
        } catch (IOException e) {
            logger.error("main(): exception[{}]", e.getMessage());
            System.exit(1);
        }
        logger.info("main():");
        System.exit(0);
    }

    /**
     * Waits after dataset import.
     */
    private static void waitAfterDatasetImport() {

        try {
            TimeUnit.SECONDS.sleep(Constants.WAIT_AFTER_DATASET_IMPORT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            logger.error("waitAfterDatasetImport(): exception[{}]", e.getMessage());
        }
    }

}