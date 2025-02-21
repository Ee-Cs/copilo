package kp.company.controller.mvc;

import kp.company.TestDatasetLoader;
import kp.company.controller.mvc.base.MVCTestsBase;
import kp.company.domain.Department;
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
import static kp.Constants.DEPARTMENTS_PATH;
import static kp.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * The tests for the {@link Department} with server-side support.
 * <p>
 * These tests use the 'Spring MVC Test Framework'.<br>
 * Loads a web application context and provides a mock web environment.<br>
 * The full Spring application context is started, but without the server.
 * </p>
 */
class DepartmentMvcTests extends MVCTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Should find all the {@link Department}s on the third page.
     * <p>
     * Tests a <b>GET</b> request.
     * </p>
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldFindAllDepartments() throws Exception {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = "%s%s%s".formatted(ROOT_URL, COMPANY_PATH, DEPARTMENTS_PATH);
        final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));
        resultActions.andExpect(jsonPath("_embedded.departmentList").isArray());
        resultActions.andExpect(jsonPath("_embedded.departmentList", Matchers.hasSize(DEP_TEST_SIZE)));
        resultActions.andExpect(
                jsonPath("_embedded.departmentList[0].id").value(Long.toString(EXPECTED_DEPARTMENT_ID)));
        resultActions.andExpect(jsonPath("_embedded.departmentList[0].name").value(EXPECTED_DEPARTMENT_NAME));
        resultActions.andExpect(
                jsonPath("_embedded.departmentList[0].employees[0].id").value(EXPECTED_EMPLOYEE_ID));
        resultActions.andExpect(
                jsonPath("_embedded.departmentList[0].employees[0].firstName").value(EXPECTED_EMPLOYEE_F_NAME));
        resultActions.andExpect(
                jsonPath("_embedded.departmentList[0].employees[0].lastName").value(EXPECTED_EMPLOYEE_L_NAME));
        resultActions.andExpect(
                jsonPath("_embedded.departmentList[0]._links.self.href").value(urlTemplate + "/1"));
        resultActions.andExpect(
                jsonPath("_embedded.departmentList[0]._links.departments.href").value(urlTemplate));
        logger.info("shouldFindAllDepartments():");
    }

    /**
     * Should get '404 Not Found' error response when searching with the absent {@link Department}.
     * <p>
     * Tests a <b>GET</b> request.
     * </p>
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldGetNotFoundErrorWhenSearchingWithAbsentDepartment() throws Exception {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = "%s%s%s/%d".formatted(ROOT_URL, COMPANY_PATH, DEPARTMENTS_PATH, ABSENT_DEPARTMENT_ID);
        final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
                .content(UPDATED_DEPARTMENT_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isNotFound());
        logger.info("shouldGetNotFoundErrorWhenSearchingWithAbsentDepartment():");
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
        final String urlTemplate = "%s%s%s/ABC".formatted(ROOT_URL, COMPANY_PATH, DEPARTMENTS_PATH);
        final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
                .content(UPDATED_DEPARTMENT_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
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
     * Should create the {@link Department}.
     * <p>
     * Tests a <b>POST</b> request.
     * </p>
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldCreateDepartment() throws Exception {
        // GIVEN
        final String urlTemplate = "%s%s%s".formatted(ROOT_URL, COMPANY_PATH, DEPARTMENTS_PATH);
        final MockHttpServletRequestBuilder requestBuilder = post(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
                .content(CREATED_DEPARTMENT_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isCreated());
        final String createdDepartmentUrlTemplate = "%s/%d".formatted(urlTemplate, CREATED_DEPARTMENT_ID);
        resultActions.andExpect(header().string(HttpHeaders.LOCATION, createdDepartmentUrlTemplate));
        resultActions.andExpect(redirectedUrl(createdDepartmentUrlTemplate));
        getAndCheckDepartment(createdDepartmentUrlTemplate, CREATED_DEPARTMENT_NAME);
        logger.info("shouldCreateDepartment():");
    }

    /**
     * Should update the {@link Department}.
     * <p>
     * Tests a <b>PATCH</b> request.
     * </p>
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldUpdateDepartment() throws Exception {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = "%s%s%s/%d".formatted(ROOT_URL, COMPANY_PATH, DEPARTMENTS_PATH, EXPECTED_DEPARTMENT_ID);
        final MockHttpServletRequestBuilder requestBuilder = patch(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
                .content(UPDATED_DEPARTMENT_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(header().string(HttpHeaders.LOCATION, urlTemplate));
        resultActions.andExpect(redirectedUrl(urlTemplate));
        getAndCheckDepartment(urlTemplate, UPDATED_DEPARTMENT_NAME);
        logger.info("shouldUpdateDepartment():");
    }

    /**
     * Should get '404 Not Found' error response when updating the absent {@link Department}.
     * <p>
     * Tests a <b>PATCH</b> request.
     * </p>
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldGetNotFoundErrorWhenUpdatingAbsentDepartment() throws Exception {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = "%s%s%s/%d".formatted(ROOT_URL, COMPANY_PATH, DEPARTMENTS_PATH, ABSENT_DEPARTMENT_ID);
        final MockHttpServletRequestBuilder requestBuilder = patch(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE)
                .content(UPDATED_DEPARTMENT_CONTENT).contentType(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isNotFound());
        logger.info("shouldGetNotFoundErrorWhenUpdatingAbsentDepartment():");
    }

    /**
     * Should delete the {@link Department}.
     * <p>
     * Tests a <b>DELETE</b> request.
     * </p>
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    void shouldDeleteDepartment() throws Exception {
        // GIVEN
        TestDatasetLoader.loadTestDataset();
        final String urlTemplate = "%s%s%s/%d".formatted(ROOT_URL, COMPANY_PATH, DEPARTMENTS_PATH, EXPECTED_DEPARTMENT_ID);
        final MockHttpServletRequestBuilder requestBuilder = delete(urlTemplate).accept(MediaTypes.HAL_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isNoContent());
        getAndCheckDeletedDepartment(urlTemplate);
        logger.info("shouldDeleteDepartment():");
    }

    /**
     * Should retrieve and check the {@link Department}.
     * <p>
     * Tests a <b>GET</b> request.
     * </p>
     *
     * @param urlTemplate            the URL template
     * @param expectedDepartmentName the expected {@link Department} name
     * @throws Exception if any error occurs during the test execution
     */
    private void getAndCheckDepartment(String urlTemplate, String expectedDepartmentName) throws Exception {
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
        resultActions.andExpect(jsonPath("name").value(expectedDepartmentName));
        resultActions.andExpect(jsonPath("_links.self.href").value(urlTemplate));
        resultActions.andExpect(jsonPath("_links.departments.href").value(FIND_DEP_HREF));
    }

    /**
     * Should not find the deleted {@link Department}.
     * <p>
     * Tests a <b>GET</b> request.
     * </p>
     *
     * @param urlTemplate the URL template
     * @throws Exception if any error occurs during the test execution
     */
    private void getAndCheckDeletedDepartment(String urlTemplate) throws Exception {
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