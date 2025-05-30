package kp.services.servers;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import kp.proto.*;
import kp.services.clients.DepartmentService;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

/**
 * gRPC service implementation for departments. This service is registered with the gRPC server.
 */
@GrpcService
public class DepartmentServiceGrpcImpl extends DepartmentServiceGrpc.DepartmentServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());
    private final DepartmentService departmentService;

    /**
     * Parameterized constructor.
     *
     * @param departmentService the department service
     */
    public DepartmentServiceGrpcImpl(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Retrieves all departments.
     *
     * @param departmentsRequest the request for departments
     * @param responseObserver   the observer for the response
     */
    @Override
    public void getDepartments(DepartmentsRequest departmentsRequest,
                               StreamObserver<DepartmentsReply> responseObserver) {

        final Optional<List<Department>> departmentListOpt = departmentService.getDepartments();
        if (departmentListOpt.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.asException());
            logger.info("getDepartments(): departments not found");
            return;
        }
        responseObserver.onNext(DepartmentsReply.newBuilder()
                .addAllDepartment(departmentListOpt.get()).build());
        responseObserver.onCompleted();
        logger.info("getDepartments():");
    }

    /**
     * Retrieves a department by its id.
     *
     * @param departmentByIdRequest the request containing the department id
     * @param responseObserver      the observer for the response
     */
    @Override
    public void getDepartmentById(DepartmentByIdRequest departmentByIdRequest,
                                  StreamObserver<DepartmentByIdReply> responseObserver) {

        final int id = departmentByIdRequest.getId();
        final Optional<Department> departmentOpt = departmentService.getDepartmentById(id);
        if (departmentOpt.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.asException());
            logger.info("getDepartmentById(): department id[{}], department not found", id);
            return;
        }
        responseObserver.onNext(DepartmentByIdReply.newBuilder()
                .setDepartment(departmentOpt.get()).build());
        responseObserver.onCompleted();
        logger.info("getDepartmentById(): department id[{}]", id);
    }
}