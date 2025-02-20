package kp.company.mvc;

import kp.company.controller.EmployeeController;
import kp.company.domain.Title;
import kp.company.mvc.base.MVCTestsBase;
import kp.company.service.CompanyService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.invoke.MethodHandles;

import static kp.TestConstants.*;

/**
 * Application tests for an employee with server-side support.
 */
@WebMvcTest(EmployeeController.class)
class EmployeeMVCTests extends MVCTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @TestBean(methodName = "createEmployeeController")
    private EmployeeController employeeController;

    /**
     * Should list employees.
     *
     * @throws Exception the {@link Exception}
     */
    @Test
    void shouldListEmployees() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/listEmployees")
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("employees"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_FIRST_NAME)));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_LAST_NAME)));
        resultActions.andExpect(MockMvcResultMatchers.content()
                .string(Matchers.containsString(CompanyService.getTitleList().getFirst().getName())));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addEmployee"))));
        logger.info("shouldListEmployees():");
    }

    /**
     * Should start employee adding.
     *
     * @throws Exception the {@link Exception}
     */
    @Test
    void shouldStartAddingEmployee() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/startEmployeeAdding")
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addEmployee"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("firstName"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("lastName"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("title"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("save"))));
        logger.info("shouldStartAddingEmployee():");
    }

    /**
     * Should start employee editing.
     *
     * @throws Exception the {@link Exception}
     */
    @Test
    void shouldStartEditingEmployee() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/startEmployeeEditing")
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM)
                .param("employeeId", TEST_EMPLOYEE_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("editEmployee"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_FIRST_NAME)));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_LAST_NAME)));
        resultActions.andExpect(MockMvcResultMatchers.content()
                .string(Matchers.containsString(CompanyService.getTitleList().getFirst().getName())));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("save"))));
        logger.info("shouldStartEditingEmployee():");
    }

    /**
     * Should start employee editing.
     *
     * @throws Exception the {@link Exception}
     */
    @Test
    void shouldGetNotFoundErrorOnEditingAbsentEmployee() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/startEmployeeEditing")
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM)
                .param("employeeId", ABSENT_ID);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
        logger.info("shouldGetNotFoundErrorOnEditingAbsentEmployee():");
    }

    /**
     * Should save employee.
     *
     * @throws Exception the {@link Exception}
     */
    @Test
    void shouldSaveEmployee() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/finishEmployeeEditing")
                .param("save", "")
                .param("id", TEST_EMPLOYEE_ID_PARAM)
                .param("firstName", CHANGED_EMPLOYEE_FIRST_NAME)
                .param("lastName", CHANGED_EMPLOYEE_LAST_NAME)
                .param("title", Title.ANALYST.name().toUpperCase())
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        /*-
         * The HTTP response status code 302 'Temporary Redirect' is a common way of performing URL redirection.
         */
        resultActions.andExpect(MockMvcResultMatchers.status().isFound());
        resultActions.andExpect(MockMvcResultMatchers
                .redirectedUrl(String.format("/listEmployees?departmentId=%s", TEST_DEPARTMENT_ID_PARAM)));

        // GIVEN
        final MockHttpServletRequestBuilder requestBuilderRedirect = MockMvcRequestBuilders
                .get("/listEmployees")
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActionsRedirect = mockMvc.perform(requestBuilderRedirect);
        // THEN
        resultActionsRedirect.andExpect(MockMvcResultMatchers.status().isOk());
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("employees"))));
        resultActionsRedirect
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(CHANGED_EMPLOYEE_FIRST_NAME)));
        resultActionsRedirect
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(CHANGED_EMPLOYEE_LAST_NAME)));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(CompanyService.getTitleList().getFirst().getName())));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addEmployee"))));
        logger.info("shouldSaveEmployee():");
    }

    /**
     * Should validate employee and show validation error.
     *
     * @throws Exception the {@link Exception}
     */
    @Test
    void shouldValidateEmployeeAndShowValidationError() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/finishEmployeeEditing")
                .param("save", "")
                .param("id", TEST_EMPLOYEE_ID_PARAM)
                .param("title", Title.ANALYST.name().toUpperCase())
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.model().attributeHasErrors("employee"));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("editEmployee"))));
        resultActions.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("must not be blank")));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("save"))));
        logger.info("shouldValidateEmployeeAndShowValidationError():");
    }

    /**
     * Should cancel employee editing.
     *
     * @throws Exception the {@link Exception}
     */
    @Test
    void shouldCancelEditingEmployee() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/finishEmployeeEditing")
                .param("cancel", "")
                .param("id", TEST_EMPLOYEE_ID_PARAM)
                .param("firstName", EXPECTED_EMPLOYEE_FIRST_NAME)
                .param("lastName", EXPECTED_EMPLOYEE_LAST_NAME)
                .param("title", Title.ANALYST.name().toUpperCase())
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isFound());
        resultActions.andExpect(MockMvcResultMatchers
                .redirectedUrl(String.format("/listEmployees?departmentId=%s", TEST_DEPARTMENT_ID_PARAM)));

        // GIVEN
        final MockHttpServletRequestBuilder requestBuilderRedirect = MockMvcRequestBuilders
                .get("/listEmployees")
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActionsRedirect = mockMvc.perform(requestBuilderRedirect);
        // THEN
        resultActionsRedirect.andExpect(MockMvcResultMatchers.status().isOk());
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("employees"))));
        resultActionsRedirect
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
        resultActionsRedirect.andExpect(MockMvcResultMatchers.content().string(Matchers.not(
                Matchers.containsString(CHANGED_EMPLOYEE_FIRST_NAME))));
        resultActionsRedirect.andExpect(MockMvcResultMatchers.content().string(Matchers.not(
                Matchers.containsString(CHANGED_EMPLOYEE_LAST_NAME))));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_FIRST_NAME)));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_LAST_NAME)));
        resultActionsRedirect.andExpect(MockMvcResultMatchers.content()
                .string(Matchers.containsString(CompanyService.getTitleList().getFirst().getName())));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addEmployee"))));
        logger.info("shouldCancelEditingEmployee():");
    }

    /**
     * Should start employee deleting.
     *
     * @throws Exception the {@link Exception}
     */
    @Test
    void shouldStartDeletingEmployee() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/startEmployeeDeleting")
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM)
                .param("employeeId", TEST_EMPLOYEE_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(
                        Matchers.containsString(accessor.getMessage("deleteEmployee"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_FIRST_NAME)));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_LAST_NAME)));
        resultActions.andExpect(MockMvcResultMatchers.content()
                .string(Matchers.containsString(CompanyService.getTitleList().getFirst().getName())));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(
                        Matchers.containsString(accessor.getMessage("delete"))));
        logger.info("shouldStartDeletingEmployee():");
    }

    /**
     * Should delete employee.
     *
     * @throws Exception the {@link Exception}
     */
    @Test
    void shouldDeleteEmployee() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/finishEmployeeDeleting")
                .param("delete", "")
                .param("id", TEST_EMPLOYEE_ID_PARAM)
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isFound());
        resultActions.andExpect(MockMvcResultMatchers
                .redirectedUrl(String.format("/listEmployees?departmentId=%s", TEST_DEPARTMENT_ID_PARAM)));

        // GIVEN
        final MockHttpServletRequestBuilder requestBuilderRedirect = MockMvcRequestBuilders
                .get("/listEmployees")
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActionsRedirect = mockMvc.perform(requestBuilderRedirect);
        // THEN
        resultActionsRedirect.andExpect(MockMvcResultMatchers.status().isOk());
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(
                        Matchers.containsString(accessor.getMessage("employees"))));
        resultActionsRedirect
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
        resultActionsRedirect.andExpect(MockMvcResultMatchers.content()
                .string(Matchers.not(Matchers.containsString(EXPECTED_EMPLOYEE_FIRST_NAME))));
        resultActionsRedirect.andExpect(MockMvcResultMatchers.content()
                .string(Matchers.not(Matchers.containsString(EXPECTED_EMPLOYEE_LAST_NAME))));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addEmployee"))));
        logger.info("shouldDeleteEmployee():");
    }

    /**
     * Should cancel employee deleting.
     *
     * @throws Exception the {@link Exception}
     */
    @Test
    void shouldCancelDeletingEmployee() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/finishEmployeeDeleting")
                .param("cancel", "")
                .param("id", TEST_EMPLOYEE_ID_PARAM)
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isFound());
        resultActions.andExpect(MockMvcResultMatchers
                .redirectedUrl(String.format("/listEmployees?departmentId=%s", TEST_DEPARTMENT_ID_PARAM)));

        // GIVEN
        final MockHttpServletRequestBuilder requestBuilderRedirect = MockMvcRequestBuilders
                .get("/listEmployees")
                .param("departmentId", TEST_DEPARTMENT_ID_PARAM);
        // WHEN
        final ResultActions resultActionsRedirect = mockMvc.perform(requestBuilderRedirect);
        // THEN
        resultActionsRedirect.andExpect(MockMvcResultMatchers.status().isOk());
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("employees"))));
        resultActionsRedirect
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_DEPARTMENT_NAME)));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_FIRST_NAME)));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(EXPECTED_EMPLOYEE_LAST_NAME)));
        resultActionsRedirect.andExpect(MockMvcResultMatchers.content()
                .string(Matchers.containsString(CompanyService.getTitleList().getFirst().getName())));
        resultActionsRedirect.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("addEmployee"))));
        logger.info("():");
    }

}
