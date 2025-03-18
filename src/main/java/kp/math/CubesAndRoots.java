package kp.math;

import kp.utils.Printer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.LongStream;

/**
 * Computes the cubes and the roots.
 */
public class CubesAndRoots {
    private static final double EXPONENT = 3;

    /**
     * Private constructor to prevent instantiation.
     */
    private CubesAndRoots() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Computes the cubes and the roots.
     */
    public static void compute() {

        final List<Long> numberList = LongStream.rangeClosed(0, 8).boxed().toList();
        Printer.printf("Numbers %s", numberList);
        final List<Long> cubeList = computeCubes(numberList);
        computeRoots(cubeList);
        Printer.printHor();
    }

    /**
     * Computes the cubes.
     *
     * @param numberList the number list
     * @return cubeList the cube list
     */
    private static List<Long> computeCubes(List<Long> numberList) {

        final List<Long> cubeList1 = numberList.stream()
                .map(arg -> arg * arg * arg)
                .toList();
        Printer.printf("Cubes   %s", cubeList1);

        final UnaryOperator<Double> powerOperator = base -> Math.pow(base, EXPONENT);
        final List<Long> cubeList2 = cubeList1.stream()
                .map(Double::valueOf)
                .map(powerOperator)
                .map(Double::longValue)
                .toList();
        Printer.printf("Cubes   %s", cubeList2);
        return cubeList2;
    }

    /**
     * Computes the roots.
     *
     * @param cubeList the cube list
     */
    private static void computeRoots(List<Long> cubeList) {

        final Function<Double, Long> scaleAndRoundingMode = arg -> BigDecimal
                .valueOf(arg)
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();

        final List<Long> cubeRootList1 = cubeList.stream()
                .map(Double::valueOf)
                .map(Math::cbrt)
                .map(scaleAndRoundingMode)
                .toList();
        Printer.printf("Roots   %s", cubeRootList1);
        /*-
         *  Computed with 'Math.log' because computing with 'Math.pow' loses precision!
         *  The n-th root of a number x is equal with the number x in the power of 1/n.
         */
        final UnaryOperator<Double> rootOperator = base -> Math.pow(Math.E, Math.log(base) / EXPONENT);
        final List<Long> cubeRootList2 = cubeRootList1.stream()
                .map(Double::valueOf)
                .map(rootOperator)
                .map(scaleAndRoundingMode)
                .toList();
        Printer.printf("Roots   %s", cubeRootList2);
    }
}