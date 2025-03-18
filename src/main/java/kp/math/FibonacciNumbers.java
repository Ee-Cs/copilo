package kp.math;

import kp.utils.Printer;
import kp.utils.Utils;

import java.time.Instant;
import java.util.List;
import java.util.function.LongPredicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Computes the Fibonacci numbers.
 */
public class FibonacciNumbers {

    /**
     * The amount of Fibonacci numbers.
     */
    private static final int AMOUNT_OF_FIBONACCI_NUMBERS = 24;
    /**
     * The maximal value of a Fibonacci number.
     */
    private static final int MAX_FIBONACCI_NUMBER = 75025;

    /**
     * Private constructor to prevent instantiation.
     */
    private FibonacciNumbers() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Computes the Fibonacci numbers.
     */
    public static void compute() {

        computeWithFasterAlgorithm();
        computeWithSlowerAlgorithm();
        Printer.printHor();
    }

    /**
     * Computes the Fibonacci numbers with faster algorithm.
     */
    private static void computeWithFasterAlgorithm() {

        final Instant begin = Instant.now();
        final Stream<long[]> stream = Stream.iterate(new long[]{1, 1},
                prev -> new long[]{prev[1], prev[0] + prev[1]});
        final List<Long> list = stream.mapToLong(pair -> pair[1])
                .boxed().limit(AMOUNT_OF_FIBONACCI_NUMBERS).toList();
        final Instant end = Instant.now();
        Printer.printf("Fibonacci numbers %s", list);
        Printer.print(Utils.formatElapsed(begin, end));
    }

    /**
     * Computes the Fibonacci numbers with slower algorithm.
     */
    private static void computeWithSlowerAlgorithm() {

        final Instant begin = Instant.now();
        final LongPredicate predicate = number -> {
            double x = 5 * Math.pow(number, 2) + 4;
            long sqrtX = (long) Math.sqrt(x);
            if (sqrtX * sqrtX == x) {
                return true;
            }
            x -= 8;
            sqrtX = (long) Math.sqrt(x);
            return sqrtX * sqrtX == x;
        };
        final List<Long> list = LongStream.rangeClosed(1, MAX_FIBONACCI_NUMBER).filter(predicate).boxed().toList();
        final Instant end = Instant.now();
        Printer.printf("Fibonacci numbers %s", list);
        Printer.print(Utils.formatElapsed(begin, end));
    }
}