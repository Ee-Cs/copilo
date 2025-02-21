package kp.company.controller;

import kp.Constants;
import kp.SampleDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import static kp.Constants.DEP_INDEX_UPPER_BOUND;
import static kp.Constants.EMP_INDEX_UPPER_BOUND;

/**
 * The sample dataset loader controller.
 */
@RestController
@RequestMapping(Constants.COMPANY_PATH)
public class SampleDatasetLoaderController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Loads the sample dataset for the departments with the employees.
     *
     * @param depIndex the department's index upper bound
     * @param empIndex the employee's index upper bound
     * @return the dataset loading confirmation response
     */
    @GetMapping(Constants.LOAD_SAMPLE_DATASET_PATH)
    public String loadSampleDataset(Optional<Long> depIndex, Optional<Long> empIndex) {

        SampleDataset.loadDataset(depIndex.orElse(DEP_INDEX_UPPER_BOUND), empIndex.orElse(EMP_INDEX_UPPER_BOUND));
        logger.debug("loadSampleDataset():");
        return Constants.LOAD_SAMPLE_DATASET_RESULT;
    }

}
