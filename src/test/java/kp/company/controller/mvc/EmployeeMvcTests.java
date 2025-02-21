package kp.company.controller.mvc;

import kp.company.TestDatasetLoader;
import kp.company.controller.mvc.base.MVCTestsBase;
import kp.company.domain.Employee;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.lang.invoke.MethodHandles;

import static kp.Constants.COMPANY_PATH;
import static kp.Constants.EMPLOYEES_PATH;
import static kp.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * The tests for the {@link Employee} with server-side support.
 * <p>
 * These tests use the 'Spring MVC Test Framework'.<br>
 * Loads a web application context and provides a mock web environment.<br>
 * The full Spring application context is started, but without the server.
 * </p>
 */
class EmployeeMvcTests extends MVCTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Should find all the {@link Employee}s.
     * <p>
     * Tests a <b>GET</b> request.
     * </p>
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldFindAllEmployees() throws Exception {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = "%s%s%s".formatted(ROOT_URL, COMPANY_PATH, EMPLOYEES_PATH);
        final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));
        resultActions.andExpect(jsonPath("_embedded.employeeList").isArray());
        resultActions.andExpect(jsonPath("_embedded.employeeList", Matchers.hasSize(DEP_TEST_SIZE * EMP_TEST_SIZE)));
        resultActions.andExpect(jsonPath("_embedded.employeeList[0].id").value(EXPECTED_EMPLOYEE_ID));
        resultActions.andExpect(jsonPath("_embedded.employeeList[0].firstName").value(EXPECTED_EMPLOYEE_F_NAME));
        resultActions.andExpect(jsonPath("_embedded.employeeList[0].lastName").value(EXPECTED_EMPLOYEE_L_NAME));
        resultActions.andExpect(jsonPath("_embedded.employeeList[0].department").exists());
        resultActions.andExpect(jsonPath("_embedded.employeeList[0].department.id").value(EXPECTED_DEPARTMENT_ID));
        resultActions.andExpect(jsonPath("_embedded.employeeList[0].department.name").value(EXPECTED_DEPARTMENT_NAME));
        resultActions.andExpect(jsonPath("_embedded.employeeList[0]._links.self.href").value(FIND_EMP_BY_ID_HREF));
        resultActions.andExpect(jsonPath("_embedded.employeeList[0]._links.employees.href").value(FIND_EMP_HREF));
        logger.info("shouldFindAllEmployees():");
    }

    /**
     * Should get '404 Not Found' error response when searching with the absent {@link Employee}.
     * <p>
     * Tests a <b>GET</b> request.
     * </p>
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldGetNotFoundErrorWhenSearchingWithAbsentEmployee() throws Exception {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = "%s%s%s/%d".formatted(ROOT_URL, COMPANY_PATH, EMPLOYEES_PATH, ABSENT_EMPLOYEE_ID);
        final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
                .content(UPDATED_EMPLOYEE_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isNotFound());
        logger.info("shouldGetNotFoundErrorWhenSearchingWithAbsentEmployee():");
    }

    /**
     * Should get '400 Bad Request' error response when searching with bad id.
     * <p>
     * Tests a <b>GET</b> request.
     * </p>
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldGetBadRequestErrorWhenSearchingWithBadId() throws Exception {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = "%s%s%s/ABC".formatted(ROOT_URL, COMPANY_PATH, EMPLOYEES_PATH);
        final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
                .content(UPDATED_EMPLOYEE_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isBadRequest());
        logger.info("shouldGetBadRequestErrorWhenSearchingWithBadId():");
    }

    /**
     * Should create the {@link Employee}.
     * <p>
     * Tests a <b>POST</b> request.
     * </p>
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldCreateEmployee() throws Exception {
        // GIVEN
        TestDatasetLoader.loadTestDatasetOnlyWithDepartment();
        final String urlTemplate = "%s%s%s".formatted(ROOT_URL, COMPANY_PATH, EMPLOYEES_PATH);
        final MockHttpServletRequestBuilder requestBuilder = post(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
                .content(CREATED_EMPLOYEE_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isCreated());
        final String createdEmployeeUrlTemplate = "%s/%d".formatted(urlTemplate, CREATED_EMPLOYEE_ID);
        resultActions.andExpect(header().string(HttpHeaders.LOCATION, createdEmployeeUrlTemplate));
        resultActions.andExpect(redirectedUrl(createdEmployeeUrlTemplate));
        getAndCheckEmployee(createdEmployeeUrlTemplate, CREATED_EMPLOYEE_F_NAME, CREATED_EMPLOYEE_L_NAME);
        logger.info("shouldCreateEmployee():");
    }

    /**
     * Should update the {@link Employee}.
     * <p>
     * Tests a <b>PATCH</b> request.
     * </p>
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldUpdateEmployee() throws Exception {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = "%s%s%s/%d".formatted(ROOT_URL, COMPANY_PATH, EMPLOYEES_PATH, EXPECTED_EMPLOYEE_ID);
        final MockHttpServletRequestBuilder requestBuilder = patch(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
                .content(UPDATED_EMPLOYEE_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(header().string(HttpHeaders.LOCATION, urlTemplate));
        resultActions.andExpect(redirectedUrl(urlTemplate));
        getAndCheckEmployee(urlTemplate, UPDATED_EMPLOYEE_F_NAME, UPDATED_EMPLOYEE_L_NAME);
        logger.info("shouldUpdateEmployee():");
    }

    /**
     * Should get '404 Not Found' error response when updating with the absent {@link Employee}.
     * <p>
     * Tests a <b>PATCH</b> request.
     * </p>
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldGetNotFoundErrorWhenUpdatingWithAbsentEmployee() throws Exception {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = "%s%s%s/%d".formatted(ROOT_URL, COMPANY_PATH, EMPLOYEES_PATH, ABSENT_EMPLOYEE_ID);
        final MockHttpServletRequestBuilder requestBuilder = patch(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
                .content(UPDATED_EMPLOYEE_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isNotFound());
        logger.info("shouldGetNotFoundErrorWhenUpdatingWithAbsentEmployee():");
    }

    /**
     * Should delete the {@link Employee}.
     * <p>
     * Tests a <b>DELETE</b> request.
     * </p>
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldDeleteEmployee() throws Exception {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = "%s%s%s/%d".formatted(ROOT_URL, COMPANY_PATH, EMPLOYEES_PATH, EXPECTED_EMPLOYEE_ID);
        final MockHttpServletRequestBuilder requestBuilder = delete(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isNoContent());
        getAndCheckDeletedEmployee(urlTemplate);
        logger.info("shouldDeleteEmployee():");
    }

    /**
     * Should retrieve and check the {@link Employee}.
     * <p>
     * Tests a <b>GET</b> request.
     * </p>
     *
     * @param urlTemplate               the URL template
     * @param expectedEmployeeFirstName the expected {@link Employee} first name
     * @param expectedEmployeeLastName  the expected {@link Employee} last name
     * @throws Exception if any error occurs during the test execution
     */
    private void getAndCheckEmployee(String urlTemplate, String expectedEmployeeFirstName,
                                     String expectedEmployeeLastName) throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));
        resultActions.andExpect(jsonPath("firstName").value(expectedEmployeeFirstName));
        resultActions.andExpect(jsonPath("lastName").value(expectedEmployeeLastName));
        resultActions.andExpect(jsonPath("_links.self.href").value(urlTemplate));
        resultActions.andExpect(jsonPath("_links.employees.href").value(FIND_EMP_HREF));
    }

    /**
     * Should not find the deleted {@link Employee}.
     * <p>
     * Tests a <b>GET</b> request.
     * </p>
     *
     * @param urlTemplate the URL template
     * @throws Exception if any error occurs during the test execution
     */
    private void getAndCheckDeletedEmployee(String urlTemplate) throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isNotFound());
    }

}