package kp.math;

import kp.utils.Printer;

import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

/**
 * Computes the Least Common Multiple.
 */
public class LeastCommonMultiple {

    /**
     * Private constructor to prevent instantiation.
     */
    private LeastCommonMultiple() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Computes the Least Common Multiple.
     * <p>
     * The number '2520' is the smallest number divisible by
     * all integers from 1 to 10, i.e., it is their least common multiple.
     * </p>
     */
    public static void compute() {

        final PrimitiveIterator.OfInt intIterator = IntStream.iterate(1, i -> ++i).iterator();
        final IntPredicate predicate = arg -> IntStream.rangeClosed(1, 9)
                .map(n -> arg % n).allMatch(n -> n == 0);
        // generate with endless iteration
        final OptionalInt result = IntStream
                .generate(intIterator::next)
                .filter(predicate)
                .findFirst();
        Printer.printf("The smallest number divisible by all one digit numbers is [%d]", result.orElse(0));
        Printer.printHor();
    }
}