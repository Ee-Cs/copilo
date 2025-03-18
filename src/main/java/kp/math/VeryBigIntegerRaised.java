package kp.math;

import kp.utils.Printer;
import kp.utils.Utils;

import java.math.BigInteger;
import java.time.Instant;

/**
 * Raise the big integer.
 * <p>
 * Raising BigInteger with the specified value to the specified exponent value.
 * </p>
 */
public class VeryBigIntegerRaised {

    /**
     * Private constructor to prevent instantiation.
     */
    private VeryBigIntegerRaised() {
        throw new IllegalStateException("Utility class");
    }

    private static final boolean SKIP_LONG_TIME_COMPUTING_FLAG = true;
    private static final boolean BASE_36_FLAG = false;
    /*-
     * The base36 length with max exponent: 415 380 039.
     * It was created in 2 hours 15 minutes.
     */
    private static final long[] VALUES = {
            2L,
            0x8000L, /*-            Short.MAX_VALUE + 1      */
            0x80000000L, /*-      Integer.MAX_VALUE + 1      */
            0x800000000000000L, /*- (Long.MAX_VALUE + 1) / 2 */
            3L,
            9L,
            17L,
    };
    private static final int[] EXPONENTS = {
            0xF,
            0xFF,
            0xFFF,
            0xFFFF,
            0xFFFFF,
            0xFFFFFF,
            0xFFFFFFF,
            0x7FFFFFFF - 1,/*- Integer.MAX_VALUE - 1 */
    };

    /**
     * Raises integers with different values and different exponents.
     */
    public static void compute() {

        for (long value : VALUES) {
            final BigInteger bigInteger = BigInteger.valueOf(value);
            Printer.printf("▼▼▼ To be raised the BigInteger with value DEC[%1$,23d]/HEX[%1$15X] ▼▼▼", bigInteger);
            compute(value, bigInteger);
            Printer.print("▲".repeat(96));
        }
        Printer.printHor();
    }

    /**
     * Raises integers with different exponents.
     *
     * @param valueToCheck the value to check
     * @param bigInteger   the integer
     */
    private static void compute(long valueToCheck, BigInteger bigInteger) {

        for (int exponent : EXPONENTS) {
            if (SKIP_LONG_TIME_COMPUTING_FLAG &&
                (valueToCheck == VALUES[4] && exponent == EXPONENTS[5] ||
                 valueToCheck == VALUES[5] && exponent == EXPONENTS[5] ||
                 valueToCheck == VALUES[6] && exponent == EXPONENTS[5])) {
                return;
            }
            computeWithExponent(bigInteger, exponent);
        }
    }

    /**
     * Raises integer.
     *
     * @param bigInteger the integer
     * @param exponent   the exponent
     */
    private static void computeWithExponent(BigInteger bigInteger, int exponent) {

        Instant start = Instant.now();
        try {
            final BigInteger veryBigInteger = bigInteger.pow(exponent);
            Printer.printf("►exponent[%1$,13d]/[%1$8X]◄", exponent);
            Printer.printf("bitLength[%,13d], bitCount[%,13d], ",
                    veryBigInteger.bitLength(), veryBigInteger.bitCount());
            Printer.print(Utils.formatElapsed(start, Instant.now()));

            if (BASE_36_FLAG) {
                start = Instant.now();
                String base36 = veryBigInteger.toString(Character.MAX_RADIX);
                Printer.printf("base36 length[%d], %s",
                        base36.length(), Utils.formatElapsed(start, Instant.now()));
            }
        } catch (Exception ex) {
            Printer.printf(" exponent[%1$,13d]/[%1$8X]\nEXCEPTION\t\t\t\t[%2$s]", exponent, ex.getMessage());
        }
    }
}