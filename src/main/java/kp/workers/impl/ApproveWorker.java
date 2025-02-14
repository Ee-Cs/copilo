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
import java.util.concurrent.TimeUnit;

/**
 * The Zeebe worker for the issue <b>approval</b>.
 */
@Component
public class ApproveWorker implements WorkerBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int SLEEP_SECONDS = 1;

    /**
     * {@inheritDoc}
     */
    @Override
    @JobWorker(type = "service-approve")
    public Map<String, Object> handle(ActivatedJob activatedJob) {

        sleepSeconds();

        final HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put(Constants.RESULT_KEY, Constants.RESULT_APPROVED);
        if (logger.isInfoEnabled()) {
            logger.info("handle():\n\tinput variables[{}],\n\tresult map[{}]",
                    activatedJob.getVariables(), resultMap);
        }
        return resultMap;
    }

    /**
     * Pauses for given seconds.
     */
    private void sleepSeconds() {

        try {
            TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }

}
