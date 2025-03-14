package kp.mappers.samples;

import kp.mappers.samples.builders.Bottom;
import kp.mappers.samples.items.ElementTwo;
import kp.mappers.samples.items.KeyTwo;
import kp.mappers.samples.items.ValueTwo;

import java.util.List;
import java.util.Map;

/**
 * The second.
 *
 * @param id      the id
 * @param name    the name
 * @param bottom  the {@link Bottom}
 * @param listTwo the {@link ElementTwo} list
 * @param mapTwo  the map two
 */
public record Second(long id,
                     String name,
                     Bottom bottom,
                     List<ElementTwo> listTwo,
                     Map<KeyTwo, ValueTwo> mapTwo) {
}
