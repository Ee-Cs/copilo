package kp.provider;

import kp.service.KpService;

/**
 * The provider of a simple {@link KpService} implementation.
 */
public class KpServiceProviderSimple {
    /**
     * Private constructor to prevent instantiation.
     */
    private KpServiceProviderSimple() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Obtains an instance of the {@link KpService}.
     *
     * @return the {@link KpService} instance
     */
    public static KpService provider() {
        return () -> "Message from «simple»  KP service";
    }
}