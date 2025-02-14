package kp.workers.impl;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import kp.Constants;
import kp.workers.WorkerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

/**
 * The Zeebe worker for the <b>medium</b> priority issues.
 */
@Component
public class MediumPriorityWorker implements WorkerBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * {@inheritDoc}
     */
    @Override
    @JobWorker(type = "service-priority-medium")
    public Map<String, Object> handle(ActivatedJob activatedJob) {

        final HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put(Constants.RESULT_KEY, Constants.RESULT_SOLVED);
        if (logger.isInfoEnabled()) {
            logger.info("handle():\n\tinput variables[{}],\n\tresult map[{}]",
                    activatedJob.getVariables(), resultMap);
        }
        return resultMap;
    }

}
