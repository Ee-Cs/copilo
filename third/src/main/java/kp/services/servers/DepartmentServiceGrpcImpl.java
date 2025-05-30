package kp.services.servers;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import kp.proto.*;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import static kp.Constants.DEPARTMENTS_LIST;

/**
 * The gRPC service implementation for the department.
 * This gRPC service is registered with a gRPC server.
 */
@GrpcService
public class DepartmentServiceGrpcImpl extends DepartmentServiceGrpc.DepartmentServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    /**
     * Retrieves a list of departments.
     *
     * @param departmentsRequest the departments request
     * @param responseObserver   the response observer
     */
    @Override
    public void getDepartments(DepartmentsRequest departmentsRequest,
                               StreamObserver<DepartmentsReply> responseObserver) {

        responseObserver.onNext(DepartmentsReply.newBuilder()
                .addAllDepartment(DEPARTMENTS_LIST).build());
        responseObserver.onCompleted();
        logger.info("getDepartments(): departmentList\n{}", DEPARTMENTS_LIST);
    }

    /**
     * Retrieves a department by its id.
     *
     * @param departmentByIdRequest the department by id request
     * @param responseObserver      the response observer
     */
    @Override
    public void getDepartmentById(DepartmentByIdRequest departmentByIdRequest,
                                  StreamObserver<DepartmentByIdReply> responseObserver) {

        final int id = departmentByIdRequest.getId();
        Optional<Department> departmentOpt = DEPARTMENTS_LIST.stream()
                .filter(dep -> id == dep.getId()).findFirst();
        if (departmentOpt.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.asException());
            logger.info("getDepartmentById(): department id[{}], department not found", id);
            return;
        }
        responseObserver.onNext(DepartmentByIdReply.newBuilder()
                .setDepartment(departmentOpt.get()).build());
        responseObserver.onCompleted();
        logger.info("getDepartmentById(): department id[{}], department\n{}",
                id, departmentOpt.get());
    }
}