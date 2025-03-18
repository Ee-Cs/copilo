package kp.math.means.bayes;

import kp.utils.Printer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The launcher of the randomized trials.
 */
public class RandomizedTrialsLauncher {
    private static final int NUMBER_OF_TRIALS = 1000;
    private static final int NUMBER_OF_SAMPLES = 1000;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * The sample outcome enum.
     */
    private enum OUTCOME {
        TN, FN, FP, TP
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private RandomizedTrialsLauncher() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Launches series of trials.
     *
     * @param index the index
     */
    public static void launchSeriesOfTrials(int index) {

        final List<Integer> levelList = Samples.LEVELS.get(index);
        final Map<OUTCOME, Integer> aggregateMap = Arrays.stream(OUTCOME.values())
                .collect(Collectors.toMap(Function.identity(), _ -> 0));
        final Map<OUTCOME, Map<OUTCOME, Integer>> maxOutcomeMap = Arrays.stream(OUTCOME.values())
                .collect(Collectors.toMap(Function.identity(), _ -> new HashMap<>(aggregateMap)));
        IntStream.rangeClosed(1, NUMBER_OF_TRIALS).forEach(
                _ -> processTrials(levelList, aggregateMap, maxOutcomeMap));
        aggregateMap.forEach((key, value) -> aggregateMap.put(key, new BigDecimal(value)
                .divide(new BigDecimal(NUMBER_OF_TRIALS), RoundingMode.HALF_UP).intValue()));
        showResults("Avg", aggregateMap);
        List.of(OUTCOME.TN, OUTCOME.FN, OUTCOME.FP, OUTCOME.TP)
                .forEach(outcome -> showResults("Max ".concat(outcome.name()), maxOutcomeMap.get(outcome)));
        Printer.printHor();
    }

    /**
     * Processes trials.
     *
     * @param levelList     the list of the levels
     * @param aggregateMap  the map of the aggregates
     * @param maxOutcomeMap the map of the outcomes for the maximum outcome
     */
    private static void processTrials(List<Integer> levelList, Map<OUTCOME, Integer> aggregateMap,
                                      Map<OUTCOME, Map<OUTCOME, Integer>> maxOutcomeMap) {

        Map<OUTCOME, Integer> resultMap = launchTrials(levelList);
        Map<OUTCOME, Integer> outcomesMap = null;
        if (resultMap.get(OUTCOME.TN) > maxOutcomeMap.get(OUTCOME.TN).get(OUTCOME.TN)) {
            outcomesMap = maxOutcomeMap.get(OUTCOME.TN);
        } else if (resultMap.get(OUTCOME.FN) > maxOutcomeMap.get(OUTCOME.FN).get(OUTCOME.FN)) {
            outcomesMap = maxOutcomeMap.get(OUTCOME.FN);
        } else if (resultMap.get(OUTCOME.FP) > maxOutcomeMap.get(OUTCOME.FP).get(OUTCOME.FP)) {
            outcomesMap = maxOutcomeMap.get(OUTCOME.FP);
        } else if (resultMap.get(OUTCOME.TP) > maxOutcomeMap.get(OUTCOME.TP).get(OUTCOME.TP)) {
            outcomesMap = maxOutcomeMap.get(OUTCOME.TP);
        }
        for (Map.Entry<OUTCOME, Integer> entry : resultMap.entrySet()) {
            final OUTCOME outcome = entry.getKey();
            aggregateMap.put(outcome, aggregateMap.get(outcome) + entry.getValue());
            if (Objects.nonNull(outcomesMap)) {
                outcomesMap.put(outcome, entry.getValue());
            }
        }
    }

    /**
     * Launches trials with the samples.
     *
     * @param levelList the list of the levels
     * @return the result map
     */
    private static Map<OUTCOME, Integer> launchTrials(List<Integer> levelList) {

        final int together = levelList.getFirst() + levelList.get(1);
        final Map<Integer, OUTCOME> outcomeMap = IntStream.rangeClosed(1, together).boxed()
                .collect(Collectors.toMap(Function.identity(), arg -> computeOutcome(levelList, arg)));

        final IntStream randomStream = IntStream.generate(() -> SECURE_RANDOM.nextInt(together))
                .limit(NUMBER_OF_SAMPLES).map(arg -> ++arg);
        final Supplier<Map<OUTCOME, Integer>> supplier = () -> Arrays.stream(OUTCOME.values())
                .collect(Collectors.toMap(Function.identity(), _ -> 0));
        final ObjIntConsumer<Map<OUTCOME, Integer>> accumulator = (map, item) ->
                map.put(outcomeMap.get(item), map.get(outcomeMap.get(item)) + 1);
        return randomStream.collect(supplier, accumulator, Map::putAll);
    }

    /**
     * Computes the outcome.
     *
     * @param levelList the list of levels
     * @param item      the item
     * @return the outcome
     */
    private static OUTCOME computeOutcome(List<Integer> levelList, int item) {

        // 0-Ngen, 1-Pgen, 2-FN, 3-FP
        if (item <= levelList.getFirst()) {
            // Genuine Negative Ngen = TN + FP
            if (item <= levelList.getFirst() - levelList.getLast()) {
                // TN = Ngen - FP
                return OUTCOME.TN;
            } else {
                return OUTCOME.FP;
            }
        } else {
            // Genuine Positive Pgen = TP + FN
            if (item <= levelList.getFirst() + levelList.get(2)) {
                // FN
                return OUTCOME.FN;
            } else {
                return OUTCOME.TP;
            }
        }
    }

    /**
     * Displays the results in five rows:
     * <ul>
     * <li>The average values from all trials.</li>
     * <li>The values of a trial with maximal true negative value.</li>
     * <li>The values of a trial with maximal false negative value.</li>
     * <li>The values of a trial with maximal false positive value.</li>
     * <li>The values of a trial with maximal true positive value.</li>
     * </ul>
     *
     * @param label     the label
     * @param resultMap the result map
     */
    private static void showResults(String label, Map<OUTCOME, Integer> resultMap) {

        final StringBuilder strBld = new StringBuilder();
        strBld.append("%-6s ".formatted(label));
        Stream.of(OUTCOME.TN, OUTCOME.FN, OUTCOME.FP, OUTCOME.TP)
                .map(outcome -> "[%1$s]=[%2$3d] ".formatted(outcome.name(), resultMap.get(outcome)))
                .forEach(strBld::append);
        final int nRec = resultMap.get(OUTCOME.TN) + resultMap.get(OUTCOME.FN);
        final int pRec = resultMap.get(OUTCOME.TP) + resultMap.get(OUTCOME.FP);
        final int nGen = resultMap.get(OUTCOME.TN) + resultMap.get(OUTCOME.FP);
        final int pGen = resultMap.get(OUTCOME.TP) + resultMap.get(OUTCOME.FN);
        Printer.printf("%s►►► [Ngen]=[%3d] [Pgen]=[%3d] — [Nrec]=[%3d] [Prec]=[%3d] ►►► [%3d]", strBld.toString(), nGen,
                pGen, nRec, pRec, nRec + pRec);
    }
}
