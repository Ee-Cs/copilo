package kp.company.controller.client.side;

import kp.company.TestDatasetLoader;
import kp.company.controller.client.side.base.ClientSideTestsBase;
import kp.company.domain.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.invoke.MethodHandles;

import static kp.Constants.COMPANY_PATH;
import static kp.Constants.EMPLOYEES_PATH;
import static kp.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The client-side tests for the {@link Employee}.
 * <p>
 * The full-stack integration test.
 * Loads a {@link org.springframework.boot.web.context.WebServerApplicationContext} and provides a real web environment.
 * </p>
 */
class EmployeeClientSideTests extends ClientSideTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Should find the {@link Employee} by the id.
     */
    @Test
    void shouldFindEmployeeById() {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = "%s%s/%d".formatted(COMPANY_PATH, EMPLOYEES_PATH, EXPECTED_EMPLOYEE_ID);
        // WHEN
        final ResponseEntity<Employee> responseEntity = restTemplate.getForEntity(urlTemplate, Employee.class);
        // THEN
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        final Employee employee = responseEntity.getBody();
        Assertions.assertThat(employee).isNotNull();
        assertEquals(EXPECTED_EMPLOYEE_ID, employee.getId());
        assertEquals(EXPECTED_EMPLOYEE_F_NAME, employee.getFirstName());
        assertEquals(EXPECTED_EMPLOYEE_L_NAME, employee.getLastName());
        logger.info("shouldFindEmployeeById():");
    }
}