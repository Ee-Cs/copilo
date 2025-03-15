package kp.security;

import kp.utils.Printer;

import java.util.Arrays;

/**
 * Example of a secure class that does not permit subclassing.
 */
public class SecureClass {
    private static final String CLEARTEXT = "ABCDEFGHIJKLMNOP";
    private static final byte[] SECRET = {
            (byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44,
            (byte) 0x45, (byte) 0x46, (byte) 0x47, (byte) 0x48,
            (byte) 0x49, (byte) 0x4A, (byte) 0x4B, (byte) 0x4C,
            (byte) 0x4D, (byte) 0x4E, (byte) 0x4F, (byte) 0x50
    };

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * Avoids exposing constructors of sensitive classes.
     * </p>
     */
    private SecureClass() {
        // empty constructor
    }

    /**
     * The guarded construction method.
     * <p>
     * Defines static factory methods instead of public constructors.
     * </p>
     *
     * @return the new secure class
     */
    public static SecureClass newSecureClass() {
        return new SecureClass();
    }

    /**
     * Launches actions.
     */
    public void launch() {

        Printer.printf("The SECRET equals CLEARTEXT[%s]: [%b]",
                CLEARTEXT, Arrays.equals(CLEARTEXT.getBytes(), SECRET));
        Printer.printHor();
    }
}
