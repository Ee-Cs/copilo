package kp.math.means.bayes;

import java.util.List;

/**
 * Samples for the Bayes Formula presentation.
 */
@SuppressWarnings("doclint:missing")
public final class Samples {
    /**
     * The levels.
     */
    static final List<List<Integer>> LEVELS = List.of(
            /*- 'Negative Genuine', 'Positive Genuine', 'False Negative', 'False Positive' */
            List.of(50, 50, 1, 1), /*- Example 0. */
            List.of(50, 50, 0, 1), /*- Example 1. */
            List.of(50, 50, 1, 0), /*- Example 2. */
            List.of(50, 50, 0, 0), /*- Example 3. */
            List.of(99_000, 1_000, 100, 9_900), /*- Example 4. */
            List.of(99_500, 500, 5, 995), /*- Example 5. */
            List.of(99_000, 1_000, 995, 195), /*- Example 6. */
            List.of(99_000, 1_000, 200, 9_900), /*- Example 7. */
            List.of(99_000, 1_000, 1, 990), /*- Example 8. */
            List.of(90_000, 10_000, 10, 900), /*- Example 9. */
            List.of(50, 50, 10, 20), /*- Example 10. */
            List.of(40, 60, 20, 10)/*- Example 11. */
    );

    /**
     * Private constructor to prevent instantiation.
     */
    private Samples() {
        throw new IllegalStateException("Utility class");
    }
}
/*- (data encoded for total of 100 items or for total of 100 000 items)
Examples 0, 1, 2, 3:
The combination of 1% FN and 1% FP.
.......................................................................................................
Example 4:
Approximately 1% of patients have disease (99 000 patients 'Negative Genuine' vs 1 000 patients 'Positive Genuine')
(i.e. disease’s prevalence is 1%)
- patient with disease    (Pos.Gen.) has a 90% chance of a true  positive test result -> 10% false negative -> modeled as   900 TP vs    100 FN 
- patient without disease (Neg.Gen.) has a 10% chance of a false positive test result -> 90% true  negative -> modeled as 9 900 FP vs 89 100 TN
What is the probability a patient has disease given that he just had a positive test?
The answer is 8.3%.
.......................................................................................................
Example 5:
Drug test is 99% sensitive (TPR) and 99% specific (TNR)
0.5% of people are users of the drug -> modeled as 99 500 vs 500
- the    500     users has a 99% chance of a true  positive test result ->  1% false negative -> modeled as 495 TP vs      5 FN
- the 99 500 non-users has a  1% chance of a false positive test result -> 99% true  negative -> modeled as 995 FP vs 98 505 TN
  (0.01 × 99 500 = 995 false positives)
What is the probability that he is a user?
The answer is 33.2%.
.......................................................................................................
Example 6:
Approximately 1% of patients have disease (99 000 patients 'Negative Genuine' vs 1 000 patients 'Positive Genuine')
Patient is  X years old  0.2% ->    200 people 
Patient not X years old 99.8% -> 99 800 people
Probability of a person diagnosed with disease being X years old is 0.5% -> of the 1 000 people with disease, only 5 people will be X years old.
-     have disease and is  X years old ->      5 people true  positive
-     have disease and not X years old ->    995 people false negative
                                                                       -> modeled as   5 TP vs    995 FN
- not have disease and is  X years old ->    195 people false positive
- not have disease and not X years old -> 98 805 people true  negative
                                                                       -> modeled as 195 FP vs 98 805 TN
What is the probability a patient at age X has a disease?
The answer is ~2.5%.
.......................................................................................................
Example 7: (from "Confusion of the Inverse")
Give the chances of disease with a 1% prior probability of occurring. (99 000 patients 'Negative Genuine' vs 1 000 patients 'Positive Genuine')
A test can detect 80% of diseases and has a 10% false positive rate.
- patient with disease    (Pos.Gen.) has an 80% chance of a true  positive test result -> 20% false negative -> modeled as   800 TP vs    200 FN
- patient without disease (Neg.Gen.) has a 10% chance of a false positive test result -> 90% true  negative -> modeled as 9 900 FP vs 89 100 TN
What is the probability of disease given a positive test result?
The answer is ~7.5%
.......................................................................................................
Example 8: (from "Confusion of the Inverse")
Suppose 1% of the group suffer from the disease, and the rest are well. -> modeled as 99 000 vs 1 000
Screening test is 99% sensitive(TPR) and 99% specific (TNR)
- the  1 000 sick    persons has a 99% chance of a true  positive test result ->  1% false negative -> modeled as 999 TP vs      1 FN
- the 99 000 healthy persons has a  1% chance of a false positive test result -> 99% true  negative -> modeled as 990 FP vs 98 010 TN
What is the probability that an individual actually has the disease?
The answer is ~50%.
.......................................................................................................
Example 9: (to observe False Positive Paradox)
Changing rate of patients with the disease from 1% (from item 8. above) to 10% -> modeled as 90 000 vs 10 000
 1% false negative -> modeled as 9 990 TP vs     10 FN
99% true  negative -> modeled as   900 FP vs 99 100 TN
The answer goes up from ~50% to ~92%.
For  1% :  1 000 people are actually infected and  1 989 people have a positive test result.
For 10% : 10 000 people are actually infected and 10 890 people have a positive test result.
.......................................................................................................
Examples 10 and 11: (comparing two cases)
- genuine  negative 50 & positive 50
- received negative 50 & positive 50
*/