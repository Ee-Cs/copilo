package kp;

import kp.security.*;
import kp.security.ecc.EllipticCurveCryptography;

/**
 * The main application for the security research.
 */
public class Application {

    /**
     * Private constructor to prevent instantiation.
     */
    private Application() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The primary entry point for launching the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        EllipticCurveCryptography.launch();
        ChecksumsComputing.launch();
        CiphersEncryptionAndDecryption.launchAesWithGcm();
        CiphersEncryptionAndDecryption.launchAesWithCbc();
        CiphersEncryptionAndDecryption.launchChaCha20();
        CiphersEncryptionAndDecryption.launchChaCha20WithPoly1305();
        CiphersEncryptionAndDecryption.encryptToFileAndDecryptFromFile();
        DigestsComputing.launch();
        KeysAndDigestsExchanging.launch();
        MacsComputing.launch();
        SecureClass.newSecureClass().launch();
        SignaturesSigning.launch();
    }
}