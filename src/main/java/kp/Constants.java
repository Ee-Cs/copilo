package kp;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The constants.
 */
@SuppressWarnings("doclint:missing")
public final class Constants {
    /**
     * The map key parts for example maps
     */
    private static final List<String> EXAMPLE_KEY_PARTS = List.of("a", "b", "c");
    /**
     * The map keys for example maps
     */
    public static final Supplier<Stream<String>> EXAMPLE_KEY_STREAM_SUP = () -> EXAMPLE_KEY_PARTS.stream()
            .flatMap(keyPart1 -> EXAMPLE_KEY_PARTS.stream()
                    .flatMap(keyPart2 -> EXAMPLE_KEY_PARTS.stream()
                            .map(keyPart3 -> keyPart1.concat(keyPart2).concat(keyPart3))));

    /**
     * The horizontal rule.
     */
    public static final String HORIZONTAL_RULE = "- ".repeat(50);
    /**
     * The separator line.
     */
    public static final String SEPARATOR_LINE = "▀ ▄ ▀ ▄ ▀ ▄";

    /**
     * The hidden constructor
     */
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}