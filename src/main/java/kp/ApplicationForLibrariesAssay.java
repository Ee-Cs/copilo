package kp;

import kp.collections.commons.ApacheCommonsCollections;
import kp.collections.eclipse.EclipseCollections;
import kp.mappers.MapperLauncher;
import kp.trees.ConcurrentTrees;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Application for the assessment of the libraries:
 * <ol>
 * <li>Apache Commons Collections.</li>
 * <li>Concurrent Trees.</li>
 * <li>Eclipse Collections.</li>
 * <li>MapStruct (Java Mapping Framework).</li>
 * </ol>
 */
public class ApplicationForLibrariesAssay {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    /**
     * Private constructor to prevent instantiation.
     */
    private ApplicationForLibrariesAssay() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The primary entry point for launching the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        logger.info(Constants.HORIZONTAL_RULE);
        MapperLauncher.launch();

        ApacheCommonsCollections.researchBidirectionalMap();
        ApacheCommonsCollections.researchPatriciaTrie();

        ConcurrentTrees.researchRadixTree();
        ConcurrentTrees.researchSuffixTree();
        ConcurrentTrees.researchInvertedRadixTree();
        ConcurrentTrees.researchReversedRadixTree();
        ConcurrentTrees.researchPrefixesAndSuffixesGenerator();
        ConcurrentTrees.researchSolver();

        EclipseCollections.researchChunksAndPairs();
        EclipseCollections.researchMapFlipping();
        EclipseCollections.researchSetOperations();
        EclipseCollections.researchUsingDates();
        EclipseCollections.researchUsingNumbers();
        EclipseCollections.researchStacks();
    }
}
