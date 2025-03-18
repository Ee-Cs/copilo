package kp;

import kp.math.*;
import kp.math.means.Means;
import kp.math.means.SummaryStatistics;
import kp.math.means.bayes.RandomizedTrialsLauncher;
import kp.math.means.bayes.StatisticalMeasures;

/**
 * Application for mathematical research.
 */
public class Application {
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

        NumberInCompactForm.format();
        Logic.mergeMap();
        EvenAndOddNumbers.segregate();
        GoldenRatio.compute();
        FibonacciNumbers.compute();
        LeastCommonMultiple.compute();
        CubesAndRoots.compute();
        BigDecimalsFromStringRepresentation.multiply();
        VeryBigIntegerRaised.compute();
        VeryBigIntegerRandomlyGenerated.compute();
        ClampNumbersAndCompareBigDecimals.clampNumbers();
        ClampNumbersAndCompareBigDecimals.compareBigDecimals();
        Means.computeMeansWithTeeing();
        SummaryStatistics.show();
        RandomizedTrialsLauncher.launchSeriesOfTrials(0);
        StatisticalMeasures.measureSamples();
    }
}