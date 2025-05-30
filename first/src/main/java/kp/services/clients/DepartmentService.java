package kp.services.clients;

import kp.proto.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing departments via gRPC client.
 */
@Service
public class DepartmentService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private DepartmentServiceGrpc.DepartmentServiceBlockingStub departmentServiceBlockingStub;

    /**
     * Injects the gRPC blocking stub for the department service.
     *
     * @param departmentServiceBlockingStub the gRPC blocking stub for the department service
     */
    @GrpcClient("grpcServerOnSecond")
    void setDepartmentServiceBlockingStub(
            DepartmentServiceGrpc.DepartmentServiceBlockingStub departmentServiceBlockingStub) {
        this.departmentServiceBlockingStub = departmentServiceBlockingStub;
    }

    /**
     * Retrieves all departments.
     *
     * @return an {@code Optional} containing the list of departments, or empty if unavailable
     */
    public Optional<List<Department>> getDepartments() {

        final DepartmentsReply departmentsReply = departmentServiceBlockingStub.getDepartments(
                DepartmentsRequest.newBuilder().build());
        final Optional<List<Department>> departmentListOpt = Optional.of(departmentsReply)
                .map(DepartmentsReply::getDepartmentList);
        logger.info("getDepartments(): departmentList\n{}", departmentListOpt.orElseGet(List::of));
        return departmentListOpt;
    }

    /**
     * Retrieves a department by its id.
     *
     * @param id the department id
     * @return an {@code Optional} containing the department, or empty if not found
     */
    public Optional<Department> getDepartmentById(int id) {

        final DepartmentByIdRequest departmentByIdRequest = DepartmentByIdRequest.newBuilder()
                .setId(id).build();
        final DepartmentByIdReply departmentByIdReply =
                departmentServiceBlockingStub.getDepartmentById(departmentByIdRequest);
        final Optional<Department> departmentOpt = Optional.of(departmentByIdReply)
                .filter(DepartmentByIdReply::hasDepartment)
                .map(DepartmentByIdReply::getDepartment);
        logger.info("getDepartmentById(): id[{}], department\n{}",
                id, departmentOpt.orElse(null));
        return departmentOpt;
    }
}