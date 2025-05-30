package kp.controllers;

import kp.proto.Department;
import kp.services.clients.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import static kp.TestConstants.DEPARTMENTS_LIST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for the {@link DepartmentController} REST API.
 * <p>
 * Verifies handling of department-related queries via HTTP endpoints.
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
class DepartmentControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private final MockMvc mockMvc;
    @MockitoBean
    private DepartmentService departmentService;

    private static final boolean VERBOSE = false;
    private static final Department TEST_DEPARTMENT = DEPARTMENTS_LIST.getFirst();
    private static final int TEST_DEPARTMENT_ID = DEPARTMENTS_LIST.getFirst().getId();
    private static final String DEPARTMENTS_URL = "http://localhost/departments";
    private static final String DEPARTMENT_BY_ID_URL =
            String.format("%s/%d", DEPARTMENTS_URL, TEST_DEPARTMENT_ID);

    /**
     * Parameterized constructor.
     *
     * @param mockMvc the {@link MockMvc} instance
     */
    DepartmentControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    /**
     * Sets up mocks before each test.
     */
    @BeforeEach
    void setUp() {
        Mockito.when(departmentService.getDepartments()).thenReturn(Optional.of(DEPARTMENTS_LIST));
        Mockito.when(departmentService.getDepartmentById(TEST_DEPARTMENT_ID))
                .thenReturn(Optional.of(TEST_DEPARTMENT));
    }

    /**
     * Verifies retrieval of all departments via HTTP GET.
     *
     * @throws Exception if a request or assertion fails
     */
    @Test
    @DisplayName("🟩 should get all departments")
    void shouldGetAllDepartments() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = get(DEPARTMENTS_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(jsonPath("[0].id").value(TEST_DEPARTMENT.getId()));
        resultActions.andExpect(jsonPath("[0].name").value(TEST_DEPARTMENT.getName()));
        resultActions.andExpect(jsonPath("[0].employees[0].id")
                .value(TEST_DEPARTMENT.getEmployeeList().getFirst().getId()));
        resultActions.andExpect(jsonPath("[0].employees[0].firstName")
                .value(TEST_DEPARTMENT.getEmployeeList().getFirst().getFirstName()));
        logger.info("shouldGetAllDepartments():");
    }

    /**
     * Verifies retrieval of a department by id via HTTP GET.
     *
     * @throws Exception if a request or assertion fails
     */
    @Test
    @DisplayName("🟩 should get department by id")
    void shouldGetDepartmentById() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = get(DEPARTMENT_BY_ID_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(jsonPath("id").value(TEST_DEPARTMENT.getId()));
        resultActions.andExpect(jsonPath("name").value(TEST_DEPARTMENT.getName()));
        resultActions.andExpect(jsonPath("employees[0].id")
                .value(TEST_DEPARTMENT.getEmployeeList().getFirst().getId()));
        resultActions.andExpect(jsonPath("employees[0].firstName")
                .value(TEST_DEPARTMENT.getEmployeeList().getFirst().getFirstName()));
        logger.info("shouldGetDepartmentById():");
    }

    /**
     * Verifies that a '404 Not Found' error is returned
     * when querying with a nonexistent department id.
     *
     * @throws Exception if a request or assertion fails
     */
    @Test
    @DisplayName("🟥 should get 'Not Found' error on search with absent department")
    void shouldGetNotFoundErrorOnSearchWithAbsentDepartment() throws Exception {
        // GIVEN
        final String urlTemplate = String.format("%s/99999", DEPARTMENTS_URL);
        final MockHttpServletRequestBuilder requestBuilder = get(urlTemplate)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isNotFound());
        logger.info("shouldGetNotFoundErrorOnSearchWithAbsentDepartment():");
    }
}