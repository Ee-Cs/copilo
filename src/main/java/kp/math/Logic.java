package kp.math;

import kp.utils.Printer;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The logic. The remapping functions with logical operators.
 */
public class Logic {

    /**
     * Private constructor to prevent instantiation.
     */
    private Logic() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The logic values aggregated in a map.
     */
    public static void mergeMap() {

        Printer.print("Logic in remapping functions for map merging");
        final Map<String, Boolean> map = new TreeMap<>();
        List.of(true, true, false, true).forEach(valueToBeMerged -> {
            map.merge("AND", valueToBeMerged, Boolean::logicalAnd);
            map.merge("OR", valueToBeMerged, Boolean::logicalOr);
            map.merge("XOR", valueToBeMerged, Boolean::logicalXor);
            Printer.printf("Value to be merged[%5s], merged map%s", valueToBeMerged, map);
        });
        Printer.printHor();
    }
}