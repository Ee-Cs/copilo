package kp.workers;

import io.camunda.zeebe.client.api.response.ActivatedJob;

import java.util.Map;

/**
 * The base interface for the Zeebe workers.
 * <p>
 * The Zeebe workers: the components that subscribe to Zeebe to execute available jobs.
 * </p>
 */
public interface WorkerBase {
    /**
     * Handles the service task.
     *
     * @param activatedJob the {@link ActivatedJob}
     * @return the result map
     */
    Map<String, Object> handle(ActivatedJob activatedJob);

}
