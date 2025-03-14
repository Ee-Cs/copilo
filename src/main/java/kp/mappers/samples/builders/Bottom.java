package kp.mappers.samples.builders;

/**
 * The 'bottom/right' position.
 */
public class Bottom {

    private final String right;

    /**
     * Gets the right.
     *
     * @return the right
     */
    public String getRight() {
        return right;
    }

    /**
     * The constructor.
     *
     * @param builder the {@link Bottom.Builder}
     */
    protected Bottom(Bottom.Builder builder) {
        this.right = builder.right;
    }

    /**
     * Gets the builder.
     *
     * @return the {@link Bottom.Builder}
     */
    public static Bottom.Builder builder() {
        return new Bottom.Builder();
    }

    /**
     * The builder.
     */
    public static class Builder {

        private String right;

        /**
         * Default constructor.
         */
        public Builder() {
            // constructor is empty
        }

        /**
         * Sets the right
         *
         * @param right the right
         * @return the builder
         */
        public Builder right(String right) {
            this.right = right;
            return this;
        }

        /**
         * Creates the bottom.
         *
         * @return the {@link Bottom}
         */
        public Bottom create() {
            return new Bottom(this);
        }
    }

}
