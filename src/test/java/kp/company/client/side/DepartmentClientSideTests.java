package kp.company.client.side;

import kp.TestConstants;
import kp.company.client.side.base.ClientSideTestsBase;
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

import static kp.TestConstants.ABSENT_ID;
import static kp.TestConstants.TEST_DEPARTMENT_ID_PARAM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Client side tests for department.
 * <p>
 * The server is <b>started</b>.
 * </p>
 */
//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DepartmentClientSideTests extends ClientSideTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Should list departments.
     */
    @Test
    void shouldListDepartments() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/listDepartments", port);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("departments"))
                // there is given department in the list
                .contains(TestConstants.EXPECTED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("addDepartment"));
        logger.info("shouldListDepartments():");
    }

    /**
     * Should start department adding.
     */
    @Test
    void shouldStartAddingDepartment() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/startDepartmentAdding", port);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("addDepartment"))
                .contains(accessor.getMessage("name"))
                .contains(accessor.getMessage("save"));
        logger.info("shouldStartAddingDepartment():");
    }

    /**
     * Should start department editing.
     */
    @Test
    void shouldStartEditingDepartment() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/startDepartmentEditing?departmentId=%s", port,
                TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("editDepartment"))
                .contains(TestConstants.EXPECTED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("save"));
        logger.info("shouldStartEditingDepartment():");
    }

    /**
     * Should not start editing non-existent department.
     */
    @Test
    void shouldGetNotFoundErrorOnEditingAbsentDepartment() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/startDepartmentEditing?departmentId=%s", port,
                ABSENT_ID);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        logger.info("shouldGetNotFoundErrorOnEditingAbsentDepartment():");
    }

    /**
     * Should save department.
     */
    @Test
    void shouldSaveDepartment() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/finishDepartmentEditing", port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("save", "");
        paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
        paramMap.add("name", TestConstants.CHANGED_DEPARTMENT_NAME);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, new HttpHeaders());
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        /*- The HTTP response status code 302 'Temporary Redirect' is a common way of performing URL redirection. */
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/listDepartments", response.getHeaders().getLocation().getPath());

        // GIVEN
        final String requestUrlRedirect = String.format("http://localhost:%s/listDepartments", port);
        // WHEN
        final ResponseEntity<String> responseRedirect = restTemplate.getForEntity(requestUrlRedirect, String.class);
        // THEN
        assertEquals(HttpStatus.OK, responseRedirect.getStatusCode());
        final String responseBody = responseRedirect.getBody();
        assertThat(responseBody).contains(accessor.getMessage("departments"))
                // there is given saved department in the list
                .contains(TestConstants.CHANGED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("addDepartment"));
        logger.info("shouldSaveDepartment():");
    }

    /**
     * Should validate department and show validation error.
     */
    @Test
    void shouldValidateDepartmentAndShowValidationError() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/finishDepartmentEditing", port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("save", "");
        paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT_LANGUAGE, Locale.US.toLanguageTag());
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, httpHeaders);
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("editDepartment"))
                // validation error
                .contains("must not be blank")
                .contains(accessor.getMessage("save"));
        logger.info("shouldValidateDepartmentAndShowValidationError():");
    }

    /**
     * Should cancel department editing.
     */
    @Test
    void shouldCancelEditingDepartment() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/finishDepartmentEditing", port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("cancel", "");
        paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
        paramMap.add("name", TestConstants.CHANGED_DEPARTMENT_NAME);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, new HttpHeaders());
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/listDepartments", response.getHeaders().getLocation().getPath());

        // GIVEN
        final String requestUrlRedirect = String.format("http://localhost:%s/listDepartments", port);
        // WHEN
        final ResponseEntity<String> responseRedirect = restTemplate.getForEntity(requestUrlRedirect, String.class);
        // THEN
        assertEquals(HttpStatus.OK, responseRedirect.getStatusCode());
        final String responseBody = responseRedirect.getBody();
        assertThat(responseBody).contains(accessor.getMessage("departments"))
                // canceled department is not found in the list
                .doesNotContain(TestConstants.CHANGED_DEPARTMENT_NAME)
                .contains(TestConstants.EXPECTED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("addDepartment"));
        logger.info("shouldCancelEditingDepartment():");
    }

    /**
     * Should start department deleting.
     */
    @Test
    void shouldStartDeletingDepartment() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/startDepartmentDeleting?departmentId=%s", port,
                TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final String responseBody = response.getBody();
        assertThat(responseBody).contains(accessor.getMessage("deleteDepartment"))
                .contains(TestConstants.EXPECTED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("delete"));
        logger.info("shouldStartDeletingDepartment():");
    }

    /**
     * Should delete department.
     */
    @Test
    void shouldDeleteDepartment() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/finishDepartmentDeleting", port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("delete", "");
        paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, new HttpHeaders());
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/listDepartments", response.getHeaders().getLocation().getPath());

        // GIVEN
        final String requestUrlRedirect = String.format("http://localhost:%s/listDepartments", port);
        // WHEN
        final ResponseEntity<String> responseRedirect = restTemplate.getForEntity(requestUrlRedirect, String.class);
        // THEN
        assertEquals(HttpStatus.OK, responseRedirect.getStatusCode());
        final String responseBody = responseRedirect.getBody();
        assertThat(responseBody).contains(accessor.getMessage("departments"))
                // deleted department is not found in the list
                .doesNotContain(TestConstants.EXPECTED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("addDepartment"));
        logger.info("shouldDeleteDepartment():");
    }

    /**
     * Should cancel department deleting.
     */
    @Test
    void shouldCancelDeletingDepartment() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/finishDepartmentDeleting", port);
        final MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("cancel", "");
        paramMap.add("id", TEST_DEPARTMENT_ID_PARAM);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, new HttpHeaders());
        // WHEN
        final ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        // THEN
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/listDepartments", response.getHeaders().getLocation().getPath());

        // GIVEN
        final String requestUrlRedirect = String.format("http://localhost:%s/listDepartments", port);
        // WHEN
        final ResponseEntity<String> responseRedirect = restTemplate.getForEntity(requestUrlRedirect, String.class);
        // THEN
        assertEquals(HttpStatus.OK, responseRedirect.getStatusCode());
        final String responseBody = responseRedirect.getBody();
        assertThat(responseBody).contains(accessor.getMessage("departments"))
                // there is not deleted department in the list
                .contains(TestConstants.EXPECTED_DEPARTMENT_NAME)
                .contains(accessor.getMessage("addDepartment"));
        logger.info("shouldCancelDeletingDepartment():");
    }
}
