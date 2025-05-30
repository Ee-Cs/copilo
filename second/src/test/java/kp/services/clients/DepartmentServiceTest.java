package kp.services.clients;

import kp.TestConstants;
import kp.proto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

/**
 * Tests for {@link DepartmentService},
 * verifying its gRPC client integration and department retrieval logic.
 */
@ActiveProfiles("test")
@SpringBootTest
class DepartmentServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private final DepartmentService departmentService = new DepartmentService();

    @MockitoBean
    private DepartmentServiceGrpc.DepartmentServiceBlockingStub departmentServiceBlockingStub;
    private static final Department TEST_DEPARTMENT = TestConstants.DEPARTMENTS_LIST.getFirst();
    private static final int UNKNOWN_ID = 99999;

    /**
     * Sets up test data and stubs before each test.
     */
    @BeforeEach
    void setUp() {
        departmentService.setDepartmentServiceBlockingStub(departmentServiceBlockingStub);
        final DepartmentsRequest departmentsRequest = DepartmentsRequest.newBuilder().build();
        final DepartmentsReply departmentsReply = DepartmentsReply.newBuilder()
                .addAllDepartment(TestConstants.DEPARTMENTS_LIST).build();
        Mockito.when(departmentServiceBlockingStub.getDepartments(departmentsRequest))
                .thenReturn(departmentsReply);
        final DepartmentByIdRequest departmentByIdRequest = DepartmentByIdRequest.newBuilder()
                .setId(TEST_DEPARTMENT.getId()).build();
        final DepartmentByIdReply departmentByIdReply = DepartmentByIdReply.newBuilder()
                .setDepartment(TEST_DEPARTMENT).build();
        Mockito.when(departmentServiceBlockingStub.getDepartmentById(departmentByIdRequest))
                .thenReturn(departmentByIdReply);
        final DepartmentByIdRequest unknownIdRequest = DepartmentByIdRequest.newBuilder()
                .setId(UNKNOWN_ID).build();
        final DepartmentByIdReply emptyReply = DepartmentByIdReply.getDefaultInstance();
        Mockito.when(departmentServiceBlockingStub.getDepartmentById(unknownIdRequest))
                .thenReturn(emptyReply);
    }

    /**
     * Verifies retrieving all departments.
     */
    @Test
    @DisplayName("🟩 should get all departments")
    void shouldGetAllDepartments() {
        // GIVEN
        // WHEN
        final Optional<List<Department>> actualDepartmentListOpt = departmentService.getDepartments();
        // THEN
        Assertions.assertTrue(actualDepartmentListOpt.isPresent(),
                "Department list should be present");
        Assertions.assertEquals(2, actualDepartmentListOpt.get().size(),
                "Size of department list");
        Assertions.assertEquals(TestConstants.DEPARTMENTS_LIST, actualDepartmentListOpt.get(),
                "Department list contents should match expected");
        logger.info("shouldGetAllDepartments():");
    }

    /**
     * Verifies retrieving a department by known id.
     */
    @Test
    @DisplayName("🟩 should get department by id")
    void shouldGetDepartmentById() {
        // GIVEN
        // WHEN
        final Optional<Department> actualDepartmentOpt = departmentService.getDepartmentById(TEST_DEPARTMENT.getId());
        // THEN
        Assertions.assertTrue(actualDepartmentOpt.isPresent(),
                "Department should be present");
        Assertions.assertEquals(TEST_DEPARTMENT, actualDepartmentOpt.get(),
                "Returned department should match expected");
        logger.info("shouldGetDepartmentById():");
    }

    /**
     * Verifies not retrieving a department by unknown id.
     */
    @Test
    @DisplayName("🟥 should not get department by id with unknown id")
    void shouldNotGetDepartmentByIdWithUnknownId() {
        // GIVEN
        // WHEN
        final Optional<Department> actualDepartmentOpt = departmentService.getDepartmentById(UNKNOWN_ID);
        // THEN
        Assertions.assertTrue(actualDepartmentOpt.isEmpty(),
                "Department should not be found for unknown id");
        logger.info("shouldNotGetDepartmentByIdWithUnknownId():");
    }
}