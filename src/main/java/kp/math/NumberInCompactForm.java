package kp.math;

import kp.utils.Printer;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Format the number in compact form.
 */
public class NumberInCompactForm {
    private static final List<Locale> LOCALES = List.of(Locale.US, Locale.FRANCE, Locale.GERMANY,
            new Locale.Builder().setLanguage("es").setRegion("ES").build());
    private static final List<Long> NUMBERS = List.of(1_234L, 1_234_567L, 1_234_567_890L, 1_234_567_890_123L);

    /**
     * Private constructor to prevent instantiation.
     */
    private NumberInCompactForm() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Formats given number in compact form using locale.
     */
    public static void format() {

        LOCALES.forEach(locale -> {
            final NumberFormat numberFormatShort = NumberFormat.getCompactNumberInstance(locale,
                    NumberFormat.Style.SHORT);
            final NumberFormat numberFormatLong = NumberFormat.getCompactNumberInstance(locale,
                    NumberFormat.Style.LONG);

            Printer.printf("Number formatted in compact form with locale[%s]:", locale);
            NUMBERS.forEach(num -> Printer.printf("\tnumber[%13d], SHORT format[%6s], LONG format[%14s]",
                    num, numberFormatShort.format(num), numberFormatLong.format(num)));
        });
        Printer.printHor();
    }
}