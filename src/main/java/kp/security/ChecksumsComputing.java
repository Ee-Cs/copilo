package kp.security;

import kp.utils.Printer;

import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CRC32C;
import java.util.zip.Checksum;

/**
 * Computing the checksums with the implementing {@link Checksum} classes:
 * <ul>
 * <li>CRC32C</li>
 * <li>CRC32</li>
 * <li>Adler32</li>
 * </ul>
 * <p>
 * An Adler-32 checksum is almost as reliable as a CRC-32 but can be computed much faster.
 * </p>
 */
public class ChecksumsComputing {
    private static final String CONTENT = "The quick brown fox jumps over the lazy dog.";

    // CRC32C (Castagnoli) is implemented in hardware in Intel CPUs
    private static final List<Checksum> CHECKSUMS = List.of(new CRC32C(), new CRC32(), new Adler32());

    /**
     * Private constructor to prevent instantiation.
     */
    private ChecksumsComputing() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Computes the checksums with different algorithms.
     */
    public static void launch() {

        CHECKSUMS.forEach(checksum -> {
            checksum.reset();
            checksum.update(CONTENT.getBytes(), 0, CONTENT.length());
            Printer.printf("checksum algorithm[%7s], value[%10d]", checksum.getClass().getSimpleName(),
                    checksum.getValue());
        });
        Printer.printHor();
    }
}
