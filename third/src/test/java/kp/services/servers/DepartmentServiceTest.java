package kp.services.servers;

import io.grpc.internal.testing.StreamRecorder;
import kp.proto.*;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static kp.Constants.DEPARTMENTS_LIST;

/**
 * The tests for the department service.
 */
@SpringBootTest
class DepartmentServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private final DepartmentServiceGrpcImpl departmentServiceGrpc;
    private static final int UNKNOWN_ID = 99999;

    /**
     * Parameterized constructor.
     *
     * @param departmentServiceGrpc the {@link DepartmentServiceGrpcImpl}
     */
    DepartmentServiceTest(@Autowired DepartmentServiceGrpcImpl departmentServiceGrpc) {
        this.departmentServiceGrpc = departmentServiceGrpc;
    }

    /**
     * Verifies retrieving all departments.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("🟩 should get all departments")
    void shouldGetAllDepartments() throws Exception {
        // GIVEN
        final DepartmentsRequest request = DepartmentsRequest.newBuilder().build();
        final StreamRecorder<DepartmentsReply> responseObserver = StreamRecorder.create();
        // WHEN
        departmentServiceGrpc.getDepartments(request, responseObserver);
        // THEN
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            Fail.fail("The call did not terminate in time");
        }
        Assertions.assertNull(responseObserver.getError(), "the stream terminating error");
        final List<DepartmentsReply> results = responseObserver.getValues();
        Assertions.assertEquals(1, results.size(), "number of received values");
        final DepartmentsReply response = results.getFirst();
        final List<Department> actualDepartmentList = response.getDepartmentList();
        Assertions.assertEquals(DEPARTMENTS_LIST, actualDepartmentList);
        logger.info("shouldGetAllDepartments():");
    }

    /**
     * Verifies retrieving a department by id.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("🟩 should get department by id")
    void shouldGetDepartmentById() throws Exception {
        // GIVEN
        final Department expectedDepartment = DEPARTMENTS_LIST.getFirst();
        final DepartmentByIdRequest request = DepartmentByIdRequest.newBuilder()
                .setId(expectedDepartment.getId()).build();
        final StreamRecorder<DepartmentByIdReply> responseObserver = StreamRecorder.create();
        // WHEN
        departmentServiceGrpc.getDepartmentById(request, responseObserver);
        // THEN
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            Fail.fail("The call did not terminate in time");
        }
        Assertions.assertNull(responseObserver.getError(), "the stream terminating error");
        final List<DepartmentByIdReply> results = responseObserver.getValues();
        Assertions.assertEquals(1, results.size(), "number of received values");
        final DepartmentByIdReply response = results.getFirst();
        final Department actualDepartment = response.getDepartment();
        Assertions.assertEquals(expectedDepartment, actualDepartment);
        logger.info("shouldGetDepartmentById():");
    }

    /**
     * Should not get department by id with unknown id.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("🟥 should not get department by id with unknown id")
    void shouldNotGetDepartmentByIdWithUnknownId() throws Exception {
        // GIVEN
        final DepartmentByIdRequest request = DepartmentByIdRequest.newBuilder()
                .setId(UNKNOWN_ID).build();
        final StreamRecorder<DepartmentByIdReply> responseObserver = StreamRecorder.create();
        // WHEN
        departmentServiceGrpc.getDepartmentById(request, responseObserver);
        // THEN
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            Fail.fail("The call did not terminate in time");
        }
        final Throwable actualError = responseObserver.getError();
        Assertions.assertNotNull(actualError, "the stream terminating error");
        Assertions.assertEquals("NOT_FOUND", actualError.getMessage());
        logger.info("shouldNotGetDepartmentByIdWithUnknownId():");
    }
}
