package kp.math.means;

import kp.utils.Printer;
import kp.utils.Utils;

import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Displays the summary statistics.
 */
public class SummaryStatistics {
    private static final long MIN_EXPONENT = 0;
    private static final long MAX_EXPONENT = 6;

    /**
     * Private constructor to prevent instantiation.
     */
    private SummaryStatistics() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Displays the summary statistics.
     */
    public static void show() {

        Printer.printf("Generated using exponents between [%d] and [%d]", MIN_EXPONENT, MAX_EXPONENT);
        final Supplier<LongStream> raisedSupplier = () -> LongStream.rangeClosed(MIN_EXPONENT, MAX_EXPONENT)
                .map(n -> (long) Math.pow(10, n));
        raisedSupplier.get().average()
                .ifPresent(average -> Printer.printf("The arithmetic mean[%s]",
                        Utils.formatNumber((long) average)));

        final List<Long> list = raisedSupplier.get().boxed().toList();
        Printer.printf("Collected list%s", list);

        final LongSummaryStatistics summaryStatistics =
                list.stream().mapToLong(Long::longValue).summaryStatistics();
        Printer.printf("The summary statistics: minimum [%s], arithmetic mean[%s], maximum [%s]",
                Utils.formatNumber(summaryStatistics.getMin()),
                Utils.formatNumber((long) summaryStatistics.getAverage()),
                Utils.formatNumber(summaryStatistics.getMax()));
        Printer.print(summaryStatistics.toString());

        final String averageFormatted = list.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.averagingLong(Long::longValue), arg -> Utils.formatNumber(arg.longValue())));
        Printer.printf("The arithmetic mean[%s]", averageFormatted);
        Printer.printHor();
    }
}
