package kp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

/**
 * The main class.
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    /**
     * Private constructor to prevent instantiation.
     */
    private Application() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The primary entry point for launching the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        final Map<String, Map<Path, Map<Integer, Set<String>>>> datasetMap = KeywordsCollector.collectKeywords();
        DatasetWriter.writeDataset(datasetMap);
        logger.info("main(): projects in dataset [{}], keywords in list [{}]",
                datasetMap.size(), Constants.KEYWORDS_LIST.size());
    }
}