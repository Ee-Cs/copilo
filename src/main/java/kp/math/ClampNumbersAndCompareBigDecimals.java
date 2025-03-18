package kp.math;

import kp.utils.Printer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Clamps the number value to fit between minimum value and maximum value.
 * <p>
 * Compares the {@link BigDecimal} numbers.
 * </p>
 */
public class ClampNumbersAndCompareBigDecimals {
    private static final String SCL_VAL_PRC_FMT = "scale[%2d], value[%9.2f], precision[%d]";

    /**
     * Private constructor to prevent instantiation.
     */
    private ClampNumbersAndCompareBigDecimals() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Clamps the numbers.
     */
    public static void clampNumbers() {

        List.of(3, 5, 8).forEach(
                number -> Printer.printf("number[%d], clamped[%d]",
                        number, Math.clamp(number, 4, 6)));
        Printer.print("");
        List.of(3d, 5d, 8d).forEach(
                number -> Printer.printf("number[%4.2f], clamped[%4.2f]",
                        number, Math.clamp(number, Math.PI, Math.TAU)));
        Printer.printHor();
    }

    /**
     * Compares the {@link BigDecimal} numbers.
     * <p>
     * The method <b>BigDecimal::compareTo</b> is preferred to the method <b>BigDecimal::equals</b>.
     * </p>
     * <p>
     * The best way to convert a double into a BigDecimal: <b>BigDecimal.valueOf(double value)</b>
     * </p>
     * <p>
     * It is generally recommended that the constructor <b>BigDecimal(String value)</b> be used
     * in preference to the constructor <b>BigDecimal(double value)</b>
     * </p>
     */
    public static void compareBigDecimals() {

        Printer.print("► ► ► BigDecimal scale setting ◄ ◄ ◄");
        final BigDecimal bdBaseScaleZero = BigDecimal.valueOf(1234);
        Printer.printf(SCL_VAL_PRC_FMT,
                bdBaseScaleZero.scale(), bdBaseScaleZero, bdBaseScaleZero.precision());
        final BigDecimal bdBaseScalePos = bdBaseScaleZero.setScale(2, RoundingMode.HALF_UP);
        Printer.printf(SCL_VAL_PRC_FMT,
                bdBaseScalePos.scale(), bdBaseScalePos, bdBaseScalePos.precision());
        final BigDecimal bdBaseScaleNeg = bdBaseScaleZero.setScale(-2, RoundingMode.HALF_UP);
        Printer.printf(SCL_VAL_PRC_FMT,
                bdBaseScaleNeg.scale(), bdBaseScaleNeg, bdBaseScaleNeg.precision());

        Printer.print("► ► ► BigDecimal multiplied by 100 ◄ ◄ ◄");
        final BigDecimal bdMulti = bdBaseScaleZero.multiply(BigDecimal.valueOf(100));
        Printer.printf(SCL_VAL_PRC_FMT,
                bdMulti.scale(), bdMulti, bdMulti.precision());

        Printer.print("► ► ► BigDecimal divided by 100 ◄ ◄ ◄");
        final BigDecimal bdDiv = bdBaseScalePos.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        Printer.printf(SCL_VAL_PRC_FMT,
                bdDiv.scale(), bdDiv, bdDiv.precision());
        final BigDecimal bdDivScaled = bdBaseScaleZero.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        Printer.printf("scale[%2d], value[%9.2f], precision[%d]%n",
                bdDivScaled.scale(), bdDivScaled, bdDivScaled.precision());

        Printer.printf("Comparing 1st[%.2f] with 2nd[%.2f], 1st with scale '0', 2nd with scale '2'",
                bdBaseScaleZero, bdBaseScalePos);
        Printer.printf("BigDecimal::compareTo[%b], BigDecimal::equals[%b]%n",
                bdBaseScaleZero.compareTo(bdBaseScalePos) == 0, bdBaseScaleZero.equals(bdBaseScalePos));
        /*
         * Rounding mode "HALF EVEN" is sometimes known as "Banker's rounding".
         */
        Printer.print("Rounding modes HALF_UP and HALF_EVEN:");
        IntStream.rangeClosed(0, 5).mapToObj(num -> BigDecimal.valueOf(num + 0.5)).forEach(number ->
                Printer.printf("\tvalue[%.1f], HALF_UP [%.0f], HALF_EVEN [%.0f]",
                        number, number.setScale(0, RoundingMode.HALF_UP),
                        number.setScale(0, RoundingMode.HALF_EVEN))
        );
        Printer.printHor();
    }
}