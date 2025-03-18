package kp.math;

import kp.utils.Printer;
import kp.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Multiply the big decimals from string representation.
 */
public class BigDecimalsFromStringRepresentation {

    /**
     * Private constructor to prevent instantiation.
     */
    private BigDecimalsFromStringRepresentation() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Multiplies the big decimals.
     */
    public static void multiply() {

        final String[][] params = {
                {"-1.4142E-12", "-1.4142E+12"},
                {"-1.111111106E+2", "+1.111111106E+3"},
                {"1", null},
                {null, "1"},
                {null, null},
        };
        for (int i = 0; i < params.length; i++) {
            multiply(params[i][0], params[i][1]);
            if (i < params.length - 1) {
                Printer.print("");
            } else {
                Printer.printHor();
            }
        }
    }

    /**
     * Multiplies the big decimals.
     *
     * @param multiplierStr   the multiplier
     * @param multiplicandStr the multiplicand
     */
    private static void multiply(String multiplierStr, String multiplicandStr) {

        Printer.printf("String multiplier[%15s], string multiplicand[%15s]", multiplierStr, multiplicandStr);
        /* A - multiply with optionals */
        final Optional<BigDecimal> multiplier = Optional.ofNullable(multiplierStr).map(BigDecimal::new);
        final Optional<BigDecimal> multiplicand = Optional.ofNullable(multiplicandStr).map(BigDecimal::new);
        final BigDecimal productA = multiplier
                .map(arg -> arg.multiply(multiplicand.orElse(BigDecimal.ZERO)))
                .orElse(BigDecimal.ZERO);
        /* B - multiply using stream of two validated strings */
        final Stream<String> streamB = Stream.of(
                Objects.requireNonNullElse(multiplierStr, "0"),
                Objects.requireNonNullElse(multiplicandStr, "0"));
        final BigDecimal productB = streamB
                .map(BigDecimal::new)
                .reduce(BigDecimal::multiply)
                .orElse(BigDecimal.ZERO);
        /* C - multiply using stream of two optionals */
        final Stream<Optional<String>> streamC = Stream.of(
                Optional.ofNullable(multiplierStr),
                Optional.ofNullable(multiplicandStr));
        final BigDecimal productC = streamC
                .map(arg -> arg.orElse("0"))/*- guards against NoSuchElementException */
                .map(BigDecimal::new)
                .reduce(BigDecimal::multiply)
                .orElse(BigDecimal.ZERO);
        /* D - multiply using stream of map entries */
        final Map<String, String> mapD = Map.of(
                Objects.requireNonNullElse(multiplierStr, "0"),
                Objects.requireNonNullElse(multiplicandStr, "0"));
        final Function<Map.Entry<String, String>, Stream<String>> mapperE =
                arg -> Stream.of(arg.getKey(), arg.getValue());
        final BigDecimal productD = mapD
                .entrySet()
                .stream()
                .flatMap(mapperE)
                .map(BigDecimal::new)
                .reduce(BigDecimal::multiply)
                .orElse(BigDecimal.ZERO);
        /* E - multiply using two streams of big decimals */
        final Supplier<Stream<BigDecimal>> multiplierStreamSup = () -> Optional.ofNullable(multiplierStr).stream()
                .map(BigDecimal::new);
        final Supplier<Stream<BigDecimal>> multiplicandStreamSup = () -> Optional.ofNullable(multiplicandStr).stream()
                .map(BigDecimal::new);
        final Function<BigDecimal, Stream<BigDecimal>> mapper = arg -> multiplicandStreamSup.get()
                .map(arg::multiply);
        final BigDecimal productE = multiplierStreamSup.get().flatMap(mapper).findFirst()
                .orElse(BigDecimal.ZERO);/*- guards against NoSuchElementException */

        Printer.printf(" → product A[%12s] B[%12s] C[%12s] D[%12s] E[%12s]", Utils.formatBigDecimal(productA),
                Utils.formatBigDecimal(productB), Utils.formatBigDecimal(productC),
                Utils.formatBigDecimal(productD), Utils.formatBigDecimal(productE));
        Printer.printf(" → BigDecimal scaled with scale '3': product[%11s]",
                productA.setScale(3, RoundingMode.HALF_EVEN));
    }
}