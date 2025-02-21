package kp.company;

import static kp.TestConstants.CREATED_DEPARTMENT_ID;
import static kp.TestConstants.CREATED_DEPARTMENT_NAME;
import static kp.TestConstants.DEP_TEST_INDEX_LOWER_BOUND;
import static kp.TestConstants.DEP_TEST_INDEX_UPPER_BOUND;
import static kp.TestConstants.EMP_TEST_INDEX_LOWER_BOUND;
import static kp.TestConstants.EMP_TEST_INDEX_UPPER_BOUND;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import kp.SampleDataset;
import kp.company.domain.Department;

/**
 * The loader for the test dataset.
 *
 */
@Component
public class TestDatasetLoader {

	/**
	 * Loads the test dataset.
	 * 
	 */
	public static void loadTestDataset() {
		SampleDataset.loadDataset(DEP_TEST_INDEX_LOWER_BOUND, DEP_TEST_INDEX_UPPER_BOUND, EMP_TEST_INDEX_LOWER_BOUND,
				EMP_TEST_INDEX_UPPER_BOUND);
	}

	/**
	 * Loads the test dataset only with the department.
	 * 
	 */
	public static void loadTestDatasetOnlyWithDepartment() {
		SampleDataset.saveDepartment(new Department(CREATED_DEPARTMENT_ID, CREATED_DEPARTMENT_NAME, new ArrayList<>()));
	}

	/**
	 * Deletes the test dataset.
	 * 
	 */
	public static void deleteTestDataset() {
		SampleDataset.deleteDataset();
	}

}
