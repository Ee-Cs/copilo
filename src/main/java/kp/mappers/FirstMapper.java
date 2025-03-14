package kp.mappers;

import kp.mappers.samples.First;
import kp.mappers.samples.Second;
import kp.mappers.samples.items.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The mapper for the {@link First} and the {@link Second}.
 */
@Mapper(uses = {
        FirstMapper.ElementOneMapper.class,
        FirstMapper.KeyOneMapper.class,
        FirstMapper.ValueOneMapper.class})
public interface FirstMapper {
    /**
     * An instance of the interface implementation.
     */
    FirstMapper INSTANCE = Mappers.getMapper(FirstMapper.class);

    /**
     * From the {@link First} to the {@link Second}.
     *
     * @param first       the {@link First}
     * @param labelsQueue the {@link Queue} with labels
     * @return the {@link Second}
     */
    @Mapping(source = "name", target = "name", qualifiedByName = "changeNameInSecond")
    @Mapping(source = "top.left", target = "bottom.right")
    @Mapping(source = "listOne", target = "listTwo")
    @Mapping(source = "mapOne", target = "mapTwo")
    Second toSecond(First first, @Context Queue<String> labelsQueue);

    /**
     * From the {@link Second} to the {@link First}.
     *
     * @param second      the {@link Second}
     * @param labelsQueue the {@link Queue} with labels
     * @return the {@link First}
     */
    @InheritInverseConfiguration
    @Mapping(source = "name", target = "name", qualifiedByName = "changeNameInFirst")
    First fromSecond(Second second, @Context Queue<String> labelsQueue);

    /**
     * Finishing the {@link Second}.
     *
     * @param second      the {@link Second}
     * @param labelsQueue the {@link Queue} with labels
     */
    @AfterMapping
    default void afterToSecond(@MappingTarget Second second, @Context Queue<String> labelsQueue) {
        second.listTwo().add(new ElementTwo(labelsQueue.poll()));
    }

    /**
     * Finishing the {@link First}.
     *
     * @param first       the {@link First}
     * @param labelsQueue the {@link Queue} with labels
     */
    @AfterMapping
    default void afterToFirst(@MappingTarget First first, @Context Queue<String> labelsQueue) {
        first.listOne().add(new ElementOne(labelsQueue.poll()));
    }

    /**
     * Changes the name in {@link Second}.
     *
     * @param name the name
     * @return the name
     */
    @Named("changeNameInSecond")
    default String changeNameInSecond(String name) {
        return name.toUpperCase();
    }

    /**
     * Changes the name in {@link First}.
     *
     * @param name the name
     * @return the name
     */
    @Named("changeNameInFirst")
    default String changeNameInFirst(String name) {
        return Stream.of(name.split("")).sorted(Collections.reverseOrder()).collect(Collectors.joining());
    }

    /**
     * The mapper for the list elements: the {@link ElementOne} and the
     * {@link ElementTwo}.
     */
    @Mapper
    interface ElementOneMapper {
        /**
         * From the {@link ElementOne} to the {@link ElementTwo}.
         *
         * @param elementOne the {@link ElementOne}
         * @return the {@link ElementTwo}
         */
        ElementTwo toElementTwo(ElementOne elementOne);

        /**
         * From the {@link ElementTwo} to the {@link ElementOne}.
         *
         * @param elementTwo the {@link ElementTwo}
         * @return the {@link ElementOne}
         */
        ElementOne fromElementTwo(ElementTwo elementTwo);
    }

    /**
     * The mapper for the map keys: the {@link KeyOne} and the {@link KeyTwo}.
     */
    @Mapper
    interface KeyOneMapper {
        /**
         * From the key one to the key two
         *
         * @param keyOne the key one
         * @return the key two
         */
        KeyTwo toKeyTwo(KeyOne keyOne);

        /**
         * From the key two to the key one
         *
         * @param keyTwo the key two
         * @return the key one
         */
        KeyOne fromKeyTwo(KeyTwo keyTwo);

    }

    /**
     * The mapper for the map values: the {@link ValueOne} and the {@link ValueTwo}.
     */
    @Mapper
    interface ValueOneMapper {
        /**
         * From the {@link ValueOne} to the {@link ValueTwo}.
         *
         * @param valueOne the the {@link ValueOne}
         * @return the {@link ValueTwo}
         */
        ValueTwo toMapTwo(ValueOne valueOne);

        /**
         * From the {@link ValueTwo} to the {@link ValueOne}.
         *
         * @param valueTwo the {@link ValueTwo}
         * @return the {@link ValueOne}
         */
        ValueOne fromValueTwo(ValueTwo valueTwo);
    }

}
