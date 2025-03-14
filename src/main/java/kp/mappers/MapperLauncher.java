package kp.mappers;

import kp.Constants;
import kp.mappers.samples.First;
import kp.mappers.samples.Second;
import kp.mappers.samples.builders.Top;
import kp.mappers.samples.items.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Researching mapping with <b>MapStruct</b>.
 */
public class MapperLauncher {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    /**
     * Private constructor to prevent instantiation.
     */
    private MapperLauncher() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Launches the mapping research.
     */
    public static void launch() {

        final Queue<String> labelsQueue = Stream.of("X", "Y", "Z")
                .collect(Collectors.toCollection(LinkedList::new));
        final Top top = Top.builder().left("OK").create();
        final List<ElementOne> listOne = List.of(new ElementOne(labelsQueue.poll()));
        final Map<KeyOne, ValueOne> mapOne = Map.of(new KeyOne(20), new ValueOne(30));
        final First firstSource = new First(10, "abcde", top, listOne, mapOne);
        final Entry<KeyOne, ValueOne> entryOne = firstSource.mapOne().entrySet().iterator().next();
        String message = ("Source first:         id[%d], " +
                          "name[%s],     top.left[%s], listOne size[%d], mapOne key/value[%d/%d]").formatted(
                firstSource.id(), firstSource.name(), firstSource.top().getLeft(),
                firstSource.listOne().size(),
                entryOne.getKey().id(), entryOne.getValue().id());
        logger.info(message);

        final Second secondResult = FirstMapper.INSTANCE.toSecond(firstSource, labelsQueue);
        final Entry<KeyTwo, ValueTwo> entryTwo = secondResult.mapTwo().entrySet().iterator().next();
        message = ("Mapped to second:     id[%d], " +
                   "name[%s], bottom.right[%s], listTwo size[%d], mapTwo key/value[%d/%d]").formatted(
                secondResult.id(), secondResult.name(), secondResult.bottom().getRight(),
                secondResult.listTwo().size(),
                entryTwo.getKey().id(), entryTwo.getValue().id());
        logger.info(message);

        final First firstResult = FirstMapper.INSTANCE.fromSecond(secondResult, labelsQueue);
        final Entry<KeyOne, ValueOne> resultEntryOne = firstSource.mapOne().entrySet().iterator().next();
        message = ("Mapped back to first: id[%d], " +
                   "name[%s],     top.left[%s], listOne size[%d], mapOne key/value[%d/%d]").formatted(
                firstResult.id(), firstResult.name(), firstSource.top().getLeft(),
                firstResult.listOne().size(),
                resultEntryOne.getKey().id(), resultEntryOne.getValue().id());
        logger.info(message);
        logger.info(Constants.SEPARATOR_LINE);

        message = "Result first.listOne%s".formatted(firstResult.listOne());
        logger.info(message);

        logger.info(Constants.HORIZONTAL_RULE);
    }

}
