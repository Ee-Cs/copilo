package kp.security;

import kp.utils.Printer;
import kp.utils.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Computing the digests with 'SHA-2' and 'SHA-3' family algorithms.
 * <p>
 * 'SHA-3' is a subset of the broader cryptographic primitive family 'Keccak'.
 * </p>
 * <p>
 * A digest has two properties:
 * </p>
 * <ul>
 * <li>It should be computationally infeasible to find two messages that hash to the same value.</li>
 * <li>The digest should not reveal anything about the input that was used to generate it.</li>
 * </ul>
 * <p>
 * The algorithms not researched here are 'MD5' and 'SHA-1':
 * </p>
 * <ul>
 * <li>The 'MD5' has been replaced with the 'SHA'.</li>
 * <li>The 'SHA-1' message digest is flawed.</li>
 * </ul>
 */
public class DigestsComputing {
    private static final boolean VERBOSE = false;
    private static final String CONTENT = "The quick brown fox jumps over the lazy dog.";

    private static final List<String> MESSAGE_DIGEST_ALGORITHMS = List.of(
            "SHA-256", "SHA-512", /*- members of the 'SHA-2' family */
            "SHA3-224", "SHA3-256", "SHA3-384", "SHA3-512"/*- members of the 'SHA-3' family */
    );

    /**
     * Private constructor to prevent instantiation.
     */
    private DigestsComputing() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Computes two digests and compares them for equality.
     */
    public static void launch() {

        MESSAGE_DIGEST_ALGORITHMS.forEach(messageDigestAlgorithm -> {
            final byte[] digestBytesAlice = computeDigest(messageDigestAlgorithm);
            final byte[] digestBytesBob = computeDigest(messageDigestAlgorithm);
            if (VERBOSE) {
                Printer.printf("digest bytes:%n%s", Utils.bytesToHexAndUtf(digestBytesAlice));
            }
            Printer.printf("message digest algorithm[%8s], length[%d], digests are equal[%b]",
                    messageDigestAlgorithm, digestBytesAlice.length,
                    MessageDigest.isEqual(digestBytesAlice, digestBytesBob));
        });
        Printer.printHor();
    }

    /**
     * Computes the digest.
     *
     * @param messageDigestAlgorithm the algorithm of a message digest
     * @return the digest bytes
     */
    private static byte[] computeDigest(String messageDigestAlgorithm) {

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(messageDigestAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            Printer.printExc(e);
            System.exit(1);
        }
        messageDigest.update(CONTENT.getBytes());
        return messageDigest.digest();
    }
}
