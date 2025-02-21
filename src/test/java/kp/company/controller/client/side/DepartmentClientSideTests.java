package kp.company.controller.client.side;

import kp.company.TestDatasetLoader;
import kp.company.controller.client.side.base.ClientSideTestsBase;
import kp.company.domain.Department;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.invoke.MethodHandles;

import static kp.Constants.COMPANY_PATH;
import static kp.Constants.DEPARTMENTS_PATH;
import static kp.TestConstants.EXPECTED_DEPARTMENT_ID;
import static kp.TestConstants.EXPECTED_DEPARTMENT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The client-side tests for the {@link Department}.
 * <p>
 * The full-stack integration test.
 * Loads a {@link org.springframework.boot.web.context.WebServerApplicationContext} and provides a real web environment.
 * </p>
 */
class DepartmentClientSideTests extends ClientSideTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Should find the {@link Department} by the id.
     */
    @Test
    void shouldFindDepartmentById() {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = String.format("%s%s/%d", COMPANY_PATH, DEPARTMENTS_PATH, EXPECTED_DEPARTMENT_ID);
        // WHEN
        final ResponseEntity<Department> responseEntity = restTemplate.getForEntity(urlTemplate, Department.class);
        // THEN
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        final Department department = responseEntity.getBody();
        Assertions.assertThat(department).isNotNull();
        assertEquals(EXPECTED_DEPARTMENT_ID, department.getId());
        assertEquals(EXPECTED_DEPARTMENT_NAME, department.getName());
        logger.info("shouldFindDepartmentById():");
    }
}