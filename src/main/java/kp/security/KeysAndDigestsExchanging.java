package kp.security;

import kp.utils.Printer;
import kp.utils.Utils;

import javax.crypto.KeyAgreement;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * The simulation of a data exchange over an insecure net.
 * <p>
 * Only the public keys and the digest bytes are exchanged there.
 * </p>
 * <p>
 * Key agreement is a protocol by which two or more parties can establish
 * the same cryptographic keys, without having to exchange any secret information.
 * </p>
 * <p>
 * It is more common in cryptography to exchange certificates
 * containing public keys rather than the keys themselves.
 * </p>
 * <p>
 * Keys negotiated by the parties:
 * </p>
 * <ul>
 * <li>Shared AES cipher key.</li>
 * <li>HMAC shared secret.</li>
 * </ul>
 */
public class KeysAndDigestsExchanging {
    private static final boolean VERBOSE = false;

    private static final String MESSAGE_DIGEST_ALGORITHM = "SHA3-512";
    private static final String EC_DOMAIN_PARAMETERS_STANDARD_NAME = "secp256r1";
    private static KeyPair keyPairAlice = null;
    private static KeyPair keyPairBob = null;

    /**
     * Private constructor to prevent instantiation.
     */
    private KeysAndDigestsExchanging() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Launches the exchange simulation.
     */
    public static void launch() {

        try {
            computeKeyPairs();
            showKeysInBase64(keyPairAlice);

            final byte[] encodedPublicKeyAlice = keyPairAlice.getPublic().getEncoded();
            final byte[] digestBytesBob = receiveAlicePublicKeyAndSendBobDigest(encodedPublicKeyAlice);

            final byte[] encodedPublicKeyBob = keyPairBob.getPublic().getEncoded();
            final byte[] digestBytesAlice = receiveBobPublicKeyAndSendAliceDigest(encodedPublicKeyBob);

            Printer.printf("Exchanged digests are equal[%b]", MessageDigest.isEqual(digestBytesBob, digestBytesAlice));
            if (VERBOSE) {
                Printer.printf("ALICE encoded private key%n%s",
                        Utils.bytesToHexAndUtf(keyPairAlice.getPrivate().getEncoded()));
                Printer.printf("ALICE encoded public key%n%s",
                        Utils.bytesToHexAndUtf(keyPairAlice.getPublic().getEncoded()));
                Printer.printf("ALICE digest bytes%n%s", Utils.bytesToHexAndUtf(digestBytesAlice));
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                 | IllegalStateException | InvalidKeySpecException e) {
            Printer.printExc(e);
            System.exit(1);
        }
        Printer.printHor();
    }

    /**
     * Computes the key pair for Alice and Bob.
     *
     * @throws NoSuchAlgorithmException           the security exception
     * @throws InvalidAlgorithmParameterException the security exception
     */
    private static void computeKeyPairs() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(
                new ECGenParameterSpec(EC_DOMAIN_PARAMETERS_STANDARD_NAME), SecureRandom.getInstanceStrong());
        keyPairAlice = keyPairGenerator.generateKeyPair();
        keyPairBob = keyPairGenerator.generateKeyPair();
    }

    /**
     * Receives Alice public key and sends Bob digest bytes.
     *
     * @param encodedPublicKeyAlice the received encoded public key
     * @throws NoSuchAlgorithmException the security exception
     * @throws InvalidKeySpecException  the security exception
     * @throws IllegalStateException    the illegal state exception
     * @throws InvalidKeyException      the security exception
     */
    private static byte[] receiveAlicePublicKeyAndSendBobDigest(byte[] encodedPublicKeyAlice)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, InvalidKeySpecException {

        final byte[] encodedPrivateKey = keyPairBob.getPrivate().getEncoded();
        final byte[] sharedSecret = computeSharedSecret(encodedPrivateKey, encodedPublicKeyAlice);
        final byte[] digestBytes = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM).digest(sharedSecret);

        Printer.printf("BOB   shared secret length[%d], message digest length[%d]", sharedSecret.length,
                digestBytes.length);
        return digestBytes;
    }

    /**
     * Receives Bob public key and sends Alice digest bytes.
     *
     * @param encodedPublicKeyBob the received encoded public key
     * @throws NoSuchAlgorithmException the security exception
     * @throws InvalidKeySpecException  the security exception
     * @throws IllegalStateException    the illegal state exception
     * @throws InvalidKeyException      the security exception
     */
    private static byte[] receiveBobPublicKeyAndSendAliceDigest(byte[] encodedPublicKeyBob)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, InvalidKeySpecException {

        final byte[] encodedPrivateKey = keyPairAlice.getPrivate().getEncoded();
        final byte[] sharedSecret = computeSharedSecret(encodedPrivateKey, encodedPublicKeyBob);
        final byte[] digestBytes = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM).digest(sharedSecret);

        Printer.printf("ALICE shared secret length[%d], message digest length[%d]", sharedSecret.length,
                digestBytes.length);
        return digestBytes;
    }

    /**
     * Computes the shared secret.
     *
     * @param encodedPrivateKey the encoded private key
     * @param encodedPublicKey  the encoded public key
     * @return the shared secret
     * @throws NoSuchAlgorithmException the security exception
     * @throws InvalidKeyException      the security exception
     * @throws IllegalStateException    the illegal state exception
     * @throws InvalidKeySpecException  the security exception
     */
    private static byte[] computeSharedSecret(byte[] encodedPrivateKey, byte[] encodedPublicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, InvalidKeySpecException {

        final KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
        final PrivateKey privateKey = KeyFactory.getInstance("EC")
                .generatePrivate(new PKCS8EncodedKeySpec(encodedPrivateKey));
        keyAgreement.init(privateKey);
        final PublicKey publicKey = KeyFactory.getInstance("EC")
                .generatePublic(new X509EncodedKeySpec(encodedPublicKey));
        keyAgreement.doPhase(publicKey, true);
        return keyAgreement.generateSecret();
    }

    /**
     * Displays keys in <b>Base64</b> - which format is URL and Filename safe.
     *
     * @param keyPair the key pair
     */
    private static void showKeysInBase64(KeyPair keyPair) {

        final byte[] encodedPrivateKey = keyPair.getPrivate().getEncoded();
        final byte[] encodedPublicKey = keyPair.getPublic().getEncoded();
        Printer.printf("encoded private key length[%3d], encoded public key length[%3d]", encodedPrivateKey.length,
                encodedPublicKey.length);
        final String base64Private = Base64.getUrlEncoder().encodeToString(encodedPrivateKey);
        final String base64Public = Base64.getUrlEncoder().encodeToString(encodedPublicKey);
        Printer.printf("private key changed to Base64 (length[%3d]):%n [%s]", base64Private.length(), base64Private);
        Printer.printf("public  key changed to Base64 (length[%3d]):%n [%s]", base64Public.length(), base64Public);
    }
}
