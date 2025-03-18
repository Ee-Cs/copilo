package kp.math;

import kp.utils.Printer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.PrimitiveIterator;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * Computes the Golden Ratio.
 */
public class GoldenRatio {

    /**
     * Private constructor to prevent instantiation.
     */
    private GoldenRatio() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Computes the golden ratio.
     */
    public static void compute() {

        compute(3);
        compute(8);
        compute(16);
        Printer.printHor();
    }

    /**
     * Computes the golden ratio with the given scale.
     *
     * @param scale the scale of the BigDecimal
     */
    private static void compute(int scale) {

        final PrimitiveIterator.OfInt intIterator = IntStream.iterate(1, i -> ++i).iterator();
        final PrimitiveIterator.OfDouble doubleIterator =
                DoubleStream.iterate(1, n -> 1 + 1 / n).iterator();
        // generate with endless iteration
        final DoubleStream doubleStream = IntStream
                .generate(intIterator::next)
                .mapToDouble(_ -> doubleIterator.next());

        final DoubleUnaryOperator scaleAndRoundingMode = arg -> BigDecimal
                .valueOf(arg)
                .setScale(scale, RoundingMode.HALF_UP)
                .doubleValue();
        final Map<Double, Boolean> flagMap = new HashMap<>();
        // φ represents the golden ratio
        final OptionalDouble phi = doubleStream
                .map(scaleAndRoundingMode)
                .filter(arg -> {
                    flagMap.put(arg, flagMap.containsKey(arg));
                    return true;
                })
                .filter(flagMap::get)
                .findFirst();
        Printer.printf("Golden ratio φ[%.15f] computed with scale[%2d]", phi.orElse(0), scale);
    }
}