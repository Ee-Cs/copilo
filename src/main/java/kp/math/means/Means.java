package kp.math.means;

import kp.utils.Printer;

import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Computes the means with teeing.
 */
public class Means {

    /**
     * Private constructor to prevent instantiation.
     */
    private Means() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Calculates the means:
     * <ul>
     * <li>Arithmetic mean.</li>
     * <li>Harmonic mean.</li>
     * <li>Geometric mean.</li>
     * <li>Quadratic mean.</li>
     * </ul>
     */
    public static void computeMeansWithTeeing() {

        computeMeansWithTeeing(() -> IntStream.rangeClosed(1, 9).boxed());
        // Fibonacci Sequence
        computeMeansWithTeeing(() -> Stream.of(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144));
    }

    /**
     * Calculates the means for supplied data.
     *
     * @param streamSupplier the stream supplier
     */
    private static void computeMeansWithTeeing(Supplier<Stream<Integer>> streamSupplier) {

        Printer.printf("Supplied sequence%s", streamSupplier.get().toList());
        /*-
         * Harmonic Mean.
         *
         * Harmonic mean example:
         * a vehicle travels a certain distance 'D' outbound at a speed "60 km/h"
         * and returns the same distance 'D' at a speed "20 km/h",
         * then its average speed "30 km/h" is the harmonic mean
         */
        final Collector<Integer, ?, Double> harmonicMeanCollector = Collectors.teeing(
                Collectors.counting(),
                Collectors.summingDouble(arg -> 1d / arg),
                (number, sum) -> number / sum);
        final double harmonicMean = streamSupplier.get().collect(harmonicMeanCollector);
        Printer.printf(" →   Harmonic mean[%.2f]", harmonicMean);
        /*
         * Geometric Mean.
         */
        final ToDoubleFunction<Integer> mapper = Math::log;
        final Collector<Integer, ?, Double> geometricMeanCollector = Collectors.teeing(
                /*- the arithmetic mean of the logarithm-transformed values */
                Collectors.summingDouble(mapper),
                Collectors.counting(),
                (sum, number) -> Math.exp(sum / number));
        final double geometricMean = streamSupplier.get().collect(geometricMeanCollector);
        Printer.printf(" →  Geometric mean[%.2f]", geometricMean);
        /*
         * Arithmetic Mean.
         */
        final Collector<Integer, ?, Double> arithmeticMeanCollector = Collectors.teeing(
                Collectors.summingDouble(Double::valueOf),
                Collectors.counting(),
                (sum, number) -> sum / number);
        final double arithmeticMean = streamSupplier.get().collect(arithmeticMeanCollector);
        Printer.printf(" → Arithmetic mean[%.2f]", arithmeticMean);
        /*
         * Quadratic Mean (Root Mean Square).
         */
        final Collector<Integer, ?, Double> quadraticMeanCollector = Collectors.teeing(
                /*- the arithmetic mean of the logarithm-transformed values */
                Collectors.summingDouble(arg -> arg * arg),
                Collectors.counting(),
                (sum, number) -> Math.sqrt(sum / number));
        final double quadraticMean = streamSupplier.get().collect(quadraticMeanCollector);
        Printer.printf(" →  Quadratic mean[%.2f]", quadraticMean);
        Printer.printHor();
    }
}
