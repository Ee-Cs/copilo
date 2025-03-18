package kp.service;

/**
 * KP Service.
 */
public interface KpService {

    /**
     * Returns true if the {@link KpService} is simple, otherwise false.
     *
     * @return the flag
     */
    default boolean isSimple() {
        return true;
    }

    /**
     * Launches {@link KpService}
     *
     * @return the service result
     */
    String launch();
}