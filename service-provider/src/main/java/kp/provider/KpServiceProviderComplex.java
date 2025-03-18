package kp.provider;

import kp.service.KpService;

/**
 * The provider of a complex {@link KpService} implementation.
 */
public class KpServiceProviderComplex {
    /**
     * Private constructor to prevent instantiation.
     */
    private KpServiceProviderComplex() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Obtains an instance of the {@link KpService}.
     *
     * @return the {@link KpService} instance
     */
    public static KpService provider() {

        return new KpService() {
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isSimple() {
                return false;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String launch() {
                return "Message from «complex» KP service";
            }
        };
    }
}