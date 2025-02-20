package kp.company.client.side;

import kp.company.client.side.base.ClientSideTestsBase;
import kp.company.controller.EmployeeController;
import kp.company.domain.Title;
import kp.company.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.invoke.MethodHandles;
import java.util.Locale;
import java.util.Optional;

import static kp.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Client-side tests for the employee.
 */
class EmployeeClientSideTests extends ClientSideTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @TestBean(methodName = "createEmployeeController")
    private EmployeeController employeeController;

    /**
     * Should list employees.
     */
    @Test
    void shouldListEmployees() {
        // GIVEN
        final String requestUrl = "http://localhost:%d/listEmployees?departmentId=%s".formatted(
                port, TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("employees"))
                .contains(EXPECTED_DEPARTMENT_NAME)
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
        final String requestUrl = "http://localhost:%d/startEmployeeAdding?departmentId=%s".formatted(
                port, TEST_DEPARTMENT_ID_PARAM);
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
        final String requestUrl = "http://localhost:%d/startEmployeeEditing?departmentId=%s&employeeId=%s".formatted(
                port, TEST_DEPARTMENT_ID_PARAM, TEST_EMPLOYEE_ID_PARAM);
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
        final String requestUrl = "http://localhost:%d/startEmployeeEditing?departmentId=%s&employeeId=%s".formatted(
                port, TEST_DEPARTMENT_ID_PARAM, ABSENT_ID);
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
        final String requestUrl = "http://localhost:%d/finishEmployeeEditing".formatted(port);
        final HttpEntity<MultiValueMap<String, String>> request = prepareSavingRequest("save");
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("employees"))
                .contains(EXPECTED_DEPARTMENT_NAME)
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
        final String requestUrl = "http://localhost:%d/finishEmployeeEditing".formatted(port);
        final HttpEntity<MultiValueMap<String, String>> request = prepareSavingRequest("save");
        Optional.ofNullable(request.getBody()).ifPresent(map -> map.set("firstName", null));
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("editEmployee"))
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
        final String requestUrl = "http://localhost:%d/finishEmployeeEditing".formatted(port);
        final HttpEntity<MultiValueMap<String, String>> request = prepareSavingRequest("cancel");
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("employees"))
                .contains(EXPECTED_DEPARTMENT_NAME)
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
        final String requestUrl = "http://localhost:%d/startEmployeeDeleting?departmentId=%s&employeeId=%s".formatted(
                port, TEST_DEPARTMENT_ID_PARAM, TEST_EMPLOYEE_ID_PARAM);
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
        final String requestUrl = "http://localhost:%d/finishEmployeeDeleting".formatted(port);
        final HttpEntity<MultiValueMap<String, String>> request = prepareDeletingRequest("delete");
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("employees"))
                .contains(EXPECTED_DEPARTMENT_NAME)
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
        final String requestUrl = "http://localhost:%d/finishEmployeeDeleting".formatted(port);
        final HttpEntity<MultiValueMap<String, String>> request = prepareDeletingRequest("cancel");
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("employees"))
                .contains(EXPECTED_DEPARTMENT_NAME)
                .contains(EXPECTED_EMPLOYEE_FIRST_NAME)
                .contains(EXPECTED_EMPLOYEE_LAST_NAME)
                .contains(CompanyService.getTitleList().getFirst().getName())
                .contains(accessor.getMessage("addEmployee"));
        logger.info("shouldCancelDeletingEmployee():");
    }

    /**
     * Prepares a request for saving.
     *
     * @param action the action
     * @return the request
     */
    private static HttpEntity<MultiValueMap<String, String>> prepareSavingRequest(String action) {

        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add(action, "");
        paramMap.add("id", TEST_EMPLOYEE_ID_PARAM);
        paramMap.add("firstName", CHANGED_EMPLOYEE_FIRST_NAME);
        paramMap.add("lastName", CHANGED_EMPLOYEE_LAST_NAME);
        paramMap.add("title", Title.ANALYST.name().toUpperCase());
        paramMap.add("departmentId", TEST_DEPARTMENT_ID_PARAM);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT_LANGUAGE, Locale.US.toLanguageTag());
        return new HttpEntity<>(paramMap, httpHeaders);
    }

    /**
     * Prepares a request for deleting.
     *
     * @param action the action
     * @return the request
     */
    private static HttpEntity<MultiValueMap<String, String>> prepareDeletingRequest(String action) {

        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add(action, "");
        paramMap.add("id", TEST_EMPLOYEE_ID_PARAM);
        paramMap.add("departmentId", TEST_DEPARTMENT_ID_PARAM);
        return new HttpEntity<>(paramMap, new HttpHeaders());
    }


}
