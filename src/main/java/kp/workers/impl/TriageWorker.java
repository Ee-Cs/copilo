package kp.workers.impl;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import kp.Constants;
import kp.workers.WorkerBase;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * The Zeebe worker for the defect's <b>triage</b>.
 * <p>
 * Uses the {@link DmnEngine} and the {@link DmnDecision}.
 * </p>
 */
@Component
public class TriageWorker implements WorkerBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DmnEngine dmnEngine;
    private DmnDecision dmnDecision = null;

    /**
     * Constructor.
     */
    public TriageWorker() {

        dmnEngine = DmnEngineConfiguration.createDefaultDmnEngineConfiguration().buildEngine();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(Constants.DECISION_DIAGRAM)) {
            dmnDecision = dmnEngine.parseDecision(Constants.DECISION_KEY, inputStream);
        } catch (IOException e) {
            logger.error("TriageWorker(): IOException[{}]", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JobWorker(type = "service-triage")
    public Map<String, Object> handle(ActivatedJob activatedJob) {

        final VariableMap variableMap = Variables.fromMap(activatedJob.getVariablesAsMap());
        /*
         * Evaluate a decision which is implemented as a decision table.
         */
        final DmnDecisionTableResult dmnDecisionTableResult = dmnEngine.evaluateDecisionTable(dmnDecision, variableMap);
        final Long priority = Optional.ofNullable(dmnDecisionTableResult)
                .map(result -> result.collectEntries(Constants.PRIORITY_KEY))
                .filter(Predicate.not(List::isEmpty)).map(List::getFirst)
                .filter(Long.class::isInstance).map(Long.class::cast).orElse(0L);

        final HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put(Constants.PRIORITY_KEY, priority);
        resultMap.put(Constants.RESULT_KEY, Constants.RESULT_EVALUATED);
        if (logger.isInfoEnabled()) {
            logger.info("handle():\n\tinput variables[{}],\n\tresult map[{}]",
                    activatedJob.getVariables(), resultMap);
        }
        return resultMap;
    }

}
