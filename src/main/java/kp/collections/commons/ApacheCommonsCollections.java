package kp.collections.commons;

import kp.Constants;
import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Researching <a href="https://commons.apache.org/proper/commons-collections/">
 * Apache Commons Collections</a>
 * <p>
 * <b>PATRICIA</b>: Practical Algorithm to Retrieve Information Coded in Alphanumeric.<br>
 * A trie is also called digital tree, radix tree or prefix tree.
 * </p>
 */
public class ApacheCommonsCollections {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    /**
     * Private constructor to prevent instantiation.
     */
    private ApacheCommonsCollections() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Researching the {@link TreeBidiMap}.
     */
    public static void researchBidirectionalMap() {

        final OrderedBidiMap<String, String> map = new TreeBidiMap<>();
        map.putAll(Map.of("keyA", "valZ", "keyB", "valY", "keyC", "valX"));

        logger.info("TreeBidiMap:");
        String message = map.toString();
        logger.info(message);

        logger.info("\nmap inverse view:");
        final OrderedBidiMap<String, String> inverseMap = map.inverseBidiMap();
        message = inverseMap.toString();
        logger.info(message);
        logger.info(Constants.HORIZONTAL_RULE);
    }

    /**
     * Researching the {@link PatriciaTrie}.
     */
    public static void researchPatriciaTrie() {

        final AtomicInteger atomic = new AtomicInteger(100);
        final UnaryOperator<String> valueMapper = _ -> String.valueOf(atomic.incrementAndGet());
        final BinaryOperator<String> mergeFunction = (value, ignoredValue) -> {
            if (!value.equals(ignoredValue)) {
                logger.error("Map merge conflict !!!");
            }
            return value;
        };
        final Trie<String, String> patriciaTrie = Constants.EXAMPLE_KEY_STREAM_SUP.get().collect(Collectors.toMap(
                Function.identity(), valueMapper, mergeFunction, PatriciaTrie::new));
        logger.info("Keys and values in PATRICIA Trie:");
        String message = patriciaTrie.keySet().toString();
        logger.info(message);
        message = patriciaTrie.values().toString();
        logger.info(message);
        logger.info(Constants.SEPARATOR_LINE);

        final SortedMap<String, String> prefixMap = patriciaTrie.prefixMap("ab");
        logger.info("View of Trie with elements having keys prefixed by 'ab':");
        message = "%s".formatted(prefixMap.keySet());
        logger.info(message);
        logger.info(Constants.SEPARATOR_LINE);

        final String firstKey = patriciaTrie.firstKey();
        message = "First key[%s], value[%2s], its previous key[%4s], its next key[%4s]".formatted(firstKey,
                patriciaTrie.get(firstKey), patriciaTrie.previousKey(firstKey), patriciaTrie.nextKey(firstKey));
        logger.info(message);

        final String otherKey = "bbb";
        message = "Other key[%s], value[%2s], its previous key[%4s], its next key[%4s]".formatted(otherKey,
                patriciaTrie.get(otherKey), patriciaTrie.previousKey(otherKey), patriciaTrie.nextKey(otherKey));
        logger.info(message);

        final String lastKey = patriciaTrie.lastKey();
        message = "Last  key[%s], value[%2s], its previous key[%4s], its next key[%4s]".formatted(lastKey,
                patriciaTrie.get(lastKey), patriciaTrie.previousKey(lastKey), patriciaTrie.nextKey(lastKey));
        logger.info(message);
        logger.info(Constants.HORIZONTAL_RULE);
    }
}
