package kp.decisions;

import kp.Constants;
import org.assertj.core.api.Assertions;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;

import static kp.TestConstants.*;

/**
 * The tests of the decisions.
 */
class DecisionsTests {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private DmnEngine dmnEngine;
    private DmnDecision dmnDecision;

    /**
     * Prepares the decision model and parses the decision.
     *
     * @throws IOException the exception
     */
    @BeforeEach
    void parseDecision() throws IOException {

        dmnEngine = DmnEngineConfiguration.createDefaultDmnEngineConfiguration().buildEngine();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(Constants.DECISION_DIAGRAM)) {
            dmnDecision = dmnEngine.parseDecision(Constants.DECISION_KEY, inputStream);
        }
    }

    /**
     * Should decide the high priority.
     */
    @Test
    void shouldDecideHighPriority() {
        // GIVEN
        // WHEN
        final DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(dmnDecision, VARIABLES_HIGH);
        // THEN
        final Long actualPriority = result.getSingleResult().getSingleEntry();
        Assertions.assertThat(actualPriority).isEqualTo(PRIORITY_HIGH);
        logger.info("shouldDecideHighPriority():");
    }

    /**
     * Should decide the medium priority.
     */
    @Test
    void shouldDecideMediumPriority() {
        // GIVEN
        // WHEN
        final DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(dmnDecision, VARIABLES_MEDIUM);
        // THEN
        final Long actualPriority = result.getSingleResult().getSingleEntry();
        Assertions.assertThat(actualPriority).isEqualTo(PRIORITY_MEDIUM);
        logger.info("shouldDecideMediumPriority():");
    }

    /**
     * Should decide the low priority.
     */
    @Test
    void shouldDecideLowPriority() {
        // GIVEN
        // WHEN
        final DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(dmnDecision, VARIABLES_LOW);
        // THEN
        final Long actualPriority = result.getSingleResult().getSingleEntry();
        Assertions.assertThat(actualPriority).isEqualTo(PRIORITY_LOW);
        logger.info("shouldDecideLowPriority():");
    }
}
