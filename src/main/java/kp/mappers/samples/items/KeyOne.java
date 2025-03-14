package kp.mappers.samples.items;

import java.util.Objects;

/**
 * The map one key
 *
 * @param id the id
 */
public record KeyOne(long id) implements Comparable<KeyOne> {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(KeyOne keyOne) {

        if (Objects.isNull(keyOne)) {
            return 0;
        }
        return Long.compare(this.id(), keyOne.id());
    }
}
