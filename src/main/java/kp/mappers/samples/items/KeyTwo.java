package kp.mappers.samples.items;

import java.util.Objects;

/**
 * The map two key
 *
 * @param id the id
 */
public record KeyTwo(long id) implements Comparable<KeyTwo> {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(KeyTwo keyTwo) {

        if (Objects.isNull(keyTwo)) {
            return 0;
        }
        return Long.compare(this.id(), keyTwo.id());
    }

}
