package kp.math.means.bayes;

import kp.utils.Printer;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

/**
 * The presentation of statistical measures.
 */
public class StatisticalMeasures {
    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;

    /**
     * Private constructor to prevent instantiation.
     */
    private StatisticalMeasures() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Measures the samples for Bayes Formula.
     */
    public static void measureSamples() {

        for (int index = 0; index < Samples.LEVELS.size(); index++) {
            showRatesAndComputeBayesFormula(index, Samples.LEVELS.get(index));
        }
        Printer.printHor();
    }

    /**
     * Shows rates and computes Bayes Formula.
     *
     * @param index     the index
     * @param levelList the list of levels
     */
    private static void showRatesAndComputeBayesFormula(int index, List<Integer> levelList) {

        final int nGen = levelList.getFirst();
        final int pGen = levelList.get(1);
        final int fn = levelList.get(2);
        final int fp = levelList.getLast();

        final int tn = nGen - fp;
        final int tp = pGen - fn;
        final int nRec = tn + fn;
        final int pRec = tp + fp;

        // Without that cast it caused bug in 'SonarQube' with message:
        // "Cast one of the operands of this addition operation to a 'long'."
        final long together = (long) nGen + pGen;
        final BigDecimal togetherBD = BigDecimal.valueOf(together);
        final BigDecimal tnBD = BigDecimal.valueOf(tn);
        final BigDecimal tpBD = BigDecimal.valueOf(tp);
        final BigDecimal nGenBD = BigDecimal.valueOf(nGen);
        final BigDecimal pGenBD = BigDecimal.valueOf(pGen);
        final BigDecimal nRecBD = BigDecimal.valueOf(nRec);
        final BigDecimal pRecBD = BigDecimal.valueOf(pRec);
        /*
         * Prior Probability (Base Rate).
         * Probability of genuine.
         */
        final BigDecimal pGenProb = pGenBD.divide(togetherBD, MATH_CONTEXT);
        final BigDecimal nGenProb = nGenBD.divide(togetherBD, MATH_CONTEXT);
        /*
         * Posterior Probability.
         * Probability of received.
         */
        final BigDecimal pRecProb = pRecBD.divide(togetherBD, MATH_CONTEXT);
        final BigDecimal nRecProb = nRecBD.divide(togetherBD, MATH_CONTEXT);
        /*
         * Specificity - True Negative Rate.
         * TNR = TN / Ngen = TN / ( TN + FP )
         */
        final BigDecimal tnr = tnBD.divide(nGenBD, MATH_CONTEXT);
        /*
         * Sensitivity - True Positive Rate.
         * TPR = TP / Pgen = TP / ( TP + FN )
         */
        final BigDecimal tpr = tpBD.divide(pGenBD, MATH_CONTEXT);
        /*
         * Negative predictive value.
         * NPV = TN / Nrec = TN / ( TN + FN )
         */
        final BigDecimal npv = tnBD.divide(nRecBD, MATH_CONTEXT);
        /*
         * Precision - positive predictive value.
         * PPV = TP / Prec = TP / ( TP + FP )
         */
        final BigDecimal ppv = tpBD.divide(pRecBD, MATH_CONTEXT);

        Printer.printf("▼▼▼▼ index[%2d] ▼▼▼▼", index);
        Printer.printf("True Neg.[%2d], False Neg.[%2d], False Pos.[%2d], True Pos.[%2d] → ", tn, fn, fp, tp);
        Printer.printf("Neg.Genuine[%2d], Pos.Genuine[%2d], Neg.Received[%2d], Pos.Received[%2d]", nGen, pGen, nRec,
                pRec);
        Printer.printf("  «GENUINE» Negative Probability[%6.2f]%% —        «GENUINE» Positive Probability[%6.2f]%%",
                100 * nGenProb.doubleValue(), 100 * pGenProb.doubleValue());
        Printer.printf(" «received» Negative Probability[%6.2f]%% —       «received» Positive Probability[%6.2f]%%",
                100 * nRecProb.doubleValue(), 100 * pRecProb.doubleValue());
        Printer.printf("«Specificity» True Negative Rate[%6.2f]%% —      «Sensitivity» True Positive Rate[%6.2f]%%",
                100 * tnr.doubleValue(), 100 * tpr.doubleValue());
        Printer.printf("       Negative Predictive Value[%6.2f]%% — «Precision» Positive Predictive Value[%6.2f]%%",
                100 * npv.doubleValue(), 100 * ppv.doubleValue());
        Printer.print("▲▲▲▲ ▲▲▲▲ ▲▲▲▲ ▲▲▲▲");
        /*
         * Compute Bayes Formula
         */
        final BigDecimal nBayes = tnr.multiply(nGenProb, MATH_CONTEXT).divide(nRecProb, MATH_CONTEXT);
        final BigDecimal pBayes = tpr.multiply(pGenProb, MATH_CONTEXT).divide(pRecProb, MATH_CONTEXT);
        boolean nFlag = npv.compareTo(nBayes) == 0;
        boolean pFlag = ppv.compareTo(pBayes) == 0;
        if (!nFlag || !pFlag) {
            // impossible
            Printer.printf("Computed from Bayes Formula were NOT equal: negative[%b], positive[%b]", nFlag, pFlag);
        }
    }

}
