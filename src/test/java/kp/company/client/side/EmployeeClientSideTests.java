package kp.company.client.side;

import kp.company.client.side.base.ClientSideTestsBase;
import kp.company.domain.Title;
import kp.company.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.invoke.MethodHandles;
import java.util.Locale;

import static kp.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Client side tests for employee.
 * <p>
 * The server is <b>started</b>.
 * </p>
 */
//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class EmployeeClientSideTests extends ClientSideTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Should list employees.
     */
    @Test
    void shouldListEmployees() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/listEmployees?departmentId=%s", port,
                TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("employees"))
                .contains(EXPECTED_DEPARTMENT_NAME)
                // there is given employee in the list
                .contains(EXPECTED_EMPLOYEE_FIRST_NAME)
                .contains(EXPECTED_EMPLOYEE_LAST_NAME)
                .contains(CompanyService.getTitleList().getFirst().getName())
                .contains(accessor.getMessage("addEmployee"));
        logger.info("shouldListEmployees():");
    }

    /**
     * Should start employee adding.
     */
    @Test
    void shouldStartAddingEmployee() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/startEmployeeAdding?departmentId=%s", port,
                TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("addEmployee"))
                .contains(accessor.getMessage("firstName"))
                .contains(accessor.getMessage("lastName"))
                .contains(accessor.getMessage("title"))
                .contains(accessor.getMessage("save"));
        logger.info("shouldStartAddingEmployee():");
    }

    /**
     * Should start employee editing.
     */
    @Test
    void shouldStartEditingEmployee() {
        // GIVEN
        final String requestUrl = String.format(
                "http://localhost:%s/startEmployeeEditing?departmentId=%s&employeeId=%s", port,
                TEST_DEPARTMENT_ID_PARAM, TEST_EMPLOYEE_ID_PARAM);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("editEmployee"))
                .contains(EXPECTED_EMPLOYEE_FIRST_NAME)
                .contains(EXPECTED_EMPLOYEE_LAST_NAME)
                .contains(CompanyService.getTitleList().getFirst().getName())
                .contains(accessor.getMessage("save"));
        logger.info("shouldStartEditingEmployee():");
    }

    /**
     * Should not start editing non-existent employee.
     */
    @Test
    void shouldGetNotFoundErrorOnEditingAbsentEmployee() {
        // GIVEN
        final String requestUrl = String.format(
                "http://localhost:%s/startEmployeeEditing?departmentId=%s&employeeId=%s", port,
                TEST_DEPARTMENT_ID_PARAM, ABSENT_ID);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        logger.info("shouldGetNotFoundErrorOnEditingAbsentEmployee():");
    }

    /**
     * Should save employee.
     */
    @Test
    void shouldSaveEmployee() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/finishEmployeeEditing", port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("save", "");
        paramMap.add("id", TEST_EMPLOYEE_ID_PARAM);
        paramMap.add("firstName", CHANGED_EMPLOYEE_FIRST_NAME);
        paramMap.add("lastName", CHANGED_EMPLOYEE_LAST_NAME);
        paramMap.add("title", Title.ANALYST.name().toUpperCase());
        paramMap.add("departmentId", TEST_DEPARTMENT_ID_PARAM);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, new HttpHeaders());
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/listEmployees", response.getHeaders().getLocation().getPath());
        assertEquals("departmentId=" + TEST_DEPARTMENT_ID_PARAM, response.getHeaders().getLocation().getQuery());

        // GIVEN
        final String requestUrlRedirect = String.format("http://localhost:%s/listEmployees?departmentId=%s", port,
                TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResponseEntity<String> responseRedirect = restTemplate.getForEntity(requestUrlRedirect, String.class);
        // THEN
        assertEquals(HttpStatus.OK, responseRedirect.getStatusCode());
        final String responseBody = responseRedirect.getBody();
        assertThat(responseBody).contains(accessor.getMessage("employees"))
                .contains(EXPECTED_DEPARTMENT_NAME)
                // there is given employee in the list
                .contains(CHANGED_EMPLOYEE_FIRST_NAME)
                .contains(CHANGED_EMPLOYEE_LAST_NAME)
                .contains(CompanyService.getTitleList().getFirst().getName())
                .contains(accessor.getMessage("addEmployee"));
        logger.info("shouldSaveEmployee():");
    }

    /**
     * Should validate employee and show validation error.
     */
    @Test
    void shouldValidateEmployeeAndShowValidationError() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/finishEmployeeEditing", port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("save", "");
        paramMap.add("id", TEST_EMPLOYEE_ID_PARAM);
        paramMap.add("title", Title.ANALYST.name().toUpperCase());
        paramMap.add("departmentId", TEST_DEPARTMENT_ID_PARAM);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT_LANGUAGE, Locale.US.toLanguageTag());
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, httpHeaders);
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("editEmployee"))
                // validation error
                .contains("must not be blank")
                .contains(accessor.getMessage("save"));
        logger.info("shouldValidateEmployeeAndShowValidationError():");
    }

    /**
     * Should cancel employee editing.
     */
    @Test
    void shouldCancelEditingEmployee() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/finishEmployeeEditing", port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("cancel", "");
        paramMap.add("id", TEST_EMPLOYEE_ID_PARAM);
        paramMap.add("firstName", CHANGED_EMPLOYEE_FIRST_NAME);
        paramMap.add("lastName", CHANGED_EMPLOYEE_LAST_NAME);
        paramMap.add("title", Title.ANALYST.name().toUpperCase());
        paramMap.add("departmentId", TEST_DEPARTMENT_ID_PARAM);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, new HttpHeaders());
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/listEmployees", response.getHeaders().getLocation().getPath());
        assertEquals("departmentId=" + TEST_DEPARTMENT_ID_PARAM, response.getHeaders().getLocation().getQuery());

        // GIVEN
        final String requestUrlRedirect = String.format("http://localhost:%s/listEmployees?departmentId=%s", port,
                TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResponseEntity<String> responseRedirect = restTemplate.getForEntity(requestUrlRedirect, String.class);
        // THEN
        assertEquals(HttpStatus.OK, responseRedirect.getStatusCode());
        final String responseBody = responseRedirect.getBody();
        assertThat(responseBody).contains(accessor.getMessage("employees"))
                .contains(EXPECTED_DEPARTMENT_NAME)
                // canceled employee is not found in the list
                .doesNotContain(CHANGED_EMPLOYEE_FIRST_NAME)
                .doesNotContain(CHANGED_EMPLOYEE_LAST_NAME)
                .contains(EXPECTED_EMPLOYEE_FIRST_NAME)
                .contains(EXPECTED_EMPLOYEE_LAST_NAME)
                .contains(CompanyService.getTitleList().getFirst().getName())
                .contains(accessor.getMessage("addEmployee"));
        logger.info("shouldCancelEditingEmployee():");
    }

    /**
     * Should start employee deleting.
     */
    @Test
    void shouldStartDeletingEmployee() {
        // GIVEN
        final String requestUrl = String.format(
                "http://localhost:%s/startEmployeeDeleting?departmentId=%s&employeeId=%s", port,
                TEST_DEPARTMENT_ID_PARAM, TEST_EMPLOYEE_ID_PARAM);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("deleteEmployee"))
                .contains(EXPECTED_EMPLOYEE_FIRST_NAME)
                .contains(EXPECTED_EMPLOYEE_LAST_NAME)
                .contains(CompanyService.getTitleList().getFirst().getName())
                .contains(accessor.getMessage("delete"));
        logger.info("shouldStartDeletingEmployee():");
    }

    /**
     * Should delete employee.
     */
    @Test
    void shouldDeleteEmployee() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/finishEmployeeDeleting", port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("delete", "");
        paramMap.add("id", TEST_EMPLOYEE_ID_PARAM);
        paramMap.add("departmentId", TEST_DEPARTMENT_ID_PARAM);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, new HttpHeaders());
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/listEmployees", response.getHeaders().getLocation().getPath());
        assertEquals(response.getHeaders().getLocation().getQuery(), "departmentId=" + TEST_DEPARTMENT_ID_PARAM);

        // GIVEN
        final String requestUrlRedirect = String.format("http://localhost:%s/listEmployees?departmentId=%s", port,
                TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResponseEntity<String> responseRedirect = restTemplate.getForEntity(requestUrlRedirect, String.class);
        // THEN
        assertEquals(HttpStatus.OK, responseRedirect.getStatusCode());
        final String responseBody = responseRedirect.getBody();
        assertThat(responseBody).contains(accessor.getMessage("employees"))
                .contains(EXPECTED_DEPARTMENT_NAME)
                // deleted employee is not found in the list
                .doesNotContain(EXPECTED_EMPLOYEE_FIRST_NAME)
                .doesNotContain(EXPECTED_EMPLOYEE_LAST_NAME)
                .contains(accessor.getMessage("addEmployee"));
        logger.info("shouldDeleteEmployee():");
    }

    /**
     * Should cancel employee deleting.
     */
    @Test
    void shouldCancelDeletingEmployee() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/finishEmployeeDeleting", port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("cancel", "");
        paramMap.add("id", TEST_EMPLOYEE_ID_PARAM);
        paramMap.add("departmentId", TEST_DEPARTMENT_ID_PARAM);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, new HttpHeaders());
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/listEmployees", response.getHeaders().getLocation().getPath());
        assertEquals("departmentId=" + TEST_DEPARTMENT_ID_PARAM, response.getHeaders().getLocation().getQuery());

        // GIVEN
        final String requestUrlRedirect = String.format("http://localhost:%s/listEmployees?departmentId=%s", port,
                TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResponseEntity<String> responseRedirect = restTemplate.getForEntity(requestUrlRedirect, String.class);
        // THEN
        assertEquals(HttpStatus.OK, responseRedirect.getStatusCode());
        final String responseBody = responseRedirect.getBody();
        assertThat(responseBody).contains(accessor.getMessage("employees"))
                .contains(EXPECTED_DEPARTMENT_NAME)
                // there is not deleted employee in the list
                .contains(EXPECTED_EMPLOYEE_FIRST_NAME)
                .contains(EXPECTED_EMPLOYEE_LAST_NAME)
                .contains(CompanyService.getTitleList().getFirst().getName())
                .contains(accessor.getMessage("addEmployee"));
        logger.info("shouldCancelDeletingEmployee():");
    }
}
