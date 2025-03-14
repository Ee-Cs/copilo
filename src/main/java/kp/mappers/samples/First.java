package kp.mappers.samples;

import kp.mappers.samples.builders.Top;
import kp.mappers.samples.items.ElementOne;
import kp.mappers.samples.items.KeyOne;
import kp.mappers.samples.items.ValueOne;

import java.util.List;
import java.util.Map;

/**
 * The first.
 *
 * @param id      the id
 * @param name    the name
 * @param top     the {@link Top}
 * @param listOne the {@link ElementOne} list
 * @param mapOne  the map one
 */
public record First(long id,
                    String name,
                    Top top,
                    List<ElementOne> listOne,
                    Map<KeyOne, ValueOne> mapOne) {
}
