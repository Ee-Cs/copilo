package kp.company.controller;

import kp.Constants;
import kp.SampleDataset;
import kp.company.assembler.EmployeeAssembler;
import kp.company.domain.Department;
import kp.company.domain.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Optional;

/**
 * The HATEOAS RESTful web service controller for handling {@link Employee} entities.
 */
@RestController
@RequestMapping(Constants.COMPANY_PATH)
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final EmployeeAssembler assembler;

    /**
     * Parameterized constructor.
     *
     * @param assembler the {@link EmployeeAssembler}
     */
    public EmployeeController(EmployeeAssembler assembler) {
        this.assembler = assembler;
    }

    /**
     * Creates a new {@link Employee}.
     *
     * @param employee the {@link Employee}
     * @return the {@link ResponseEntity} with the {@link EntityModel} for the {@link Employee}
     */
    @PostMapping(Constants.EMPLOYEES_PATH)
    public ResponseEntity<EntityModel<Employee>> createEmployee(@RequestBody Employee employee) {

        if (Optional.ofNullable(employee).isEmpty()) {
            logger.error("createEmployee(): bad request - empty request body");
            return ResponseEntity.badRequest().build();
        }
        final Optional<Department> departmentOpt = Optional.of(employee).map(Employee::getDepartment)
                .map(Department::getId).flatMap(SampleDataset::findDepartmentById);
        if (departmentOpt.isEmpty()) {
            logger.error("createEmployee(): not found - employee has non-existing department");
            return ResponseEntity.notFound().build();
        }
        final ResponseEntity<EntityModel<Employee>> responseEntity = saveEmployee(employee);
        logger.info("createEmployee():");
        return responseEntity;
    }

    /**
     * Retrieves the {@link Employee} with the specified id.
     *
     * @param id the {@link Employee} id
     * @return the {@link ResponseEntity} with the {@link EntityModel} for the {@link Employee}
     */
    @GetMapping(Constants.EMPLOYEE_ID_PATH)
    public ResponseEntity<EntityModel<Employee>> findEmployeeById(@PathVariable("id") Long id) {

        if (Optional.ofNullable(id).isEmpty()) {
            logger.error("findEmployeeById(): bad request - empty id parameter");
            return ResponseEntity.badRequest().build();
        }
        final ResponseEntity<EntityModel<Employee>> responseEntity = SampleDataset.findEmployeeById(id)
                .map(assembler::toModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        logger.info("findEmployeeById(): employee id[{}]", id);
        return responseEntity;
    }

    /**
     * Retrieves all {@link Employee}s.
     *
     * @return the {@link ResponseEntity} with the {@link PagedModel}
     */
    @GetMapping(Constants.EMPLOYEES_PATH)
    public ResponseEntity<CollectionModel<EntityModel<Employee>>> findAllEmployees() {

        final ResponseEntity<CollectionModel<EntityModel<Employee>>> responseEntity = SampleDataset.findAllEmployees()
                .map(assembler::toCollectionModel).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        logger.info("findAllEmployees():");
        return responseEntity;
    }

    /**
     * Updates the {@link Employee} with the specified id.
     *
     * @param employee the {@link Employee}
     * @param id       the {@link Employee} id
     * @return the {@link ResponseEntity} with the {@link EntityModel} for the {@link Employee}
     */
    @PatchMapping(Constants.EMPLOYEE_ID_PATH)
    public ResponseEntity<EntityModel<Employee>> updateEmployee(
            @RequestBody Employee employee, @PathVariable("id") Long id) {

        final Optional<Employee> employeeOpt = Optional.ofNullable(employee);
        if (employeeOpt.isEmpty() || Optional.ofNullable(id).isEmpty()) {
            logger.error("updateEmployee(): bad request - empty request body or empty parameter");
            return ResponseEntity.badRequest().build();
        }
        final boolean departmentPresent = Optional.of(employee).map(Employee::getDepartment).map(Department::getId)
                .map(SampleDataset::findDepartmentById).isPresent();
        if (!departmentPresent) {
            logger.error("updateEmployee(): not found - employee has non-existing department");
            return ResponseEntity.notFound().build();
        }
        final ResponseEntity<EntityModel<Employee>> responseEntity = SampleDataset.findEmployeeById(id)
                .map(emp -> new Employee(id,
                        employeeOpt.map(Employee::getFirstName).orElse(emp.getFirstName()),
                        employeeOpt.map(Employee::getLastName).orElse(emp.getLastName()),
                        employeeOpt.map(Employee::getDepartment).orElse(emp.getDepartment())))
                .map(this::saveEmployee).orElseGet(() -> ResponseEntity.notFound().build());
        logger.info("updateEmployee(): employee id[{}]", employee.getId());
        return responseEntity;
    }

    /**
     * Deletes the {@link Employee} with the specified id.
     *
     * @param id the {@link Employee} id
     * @return the {@link ResponseEntity}
     */
    @DeleteMapping(Constants.EMPLOYEE_ID_PATH)
    public ResponseEntity<EntityModel<Employee>> deleteEmployee(@PathVariable("id") Long id) {

        if (Optional.ofNullable(id).isEmpty()) {
            logger.error("deleteEmployee(): bad request - empty id parameter");
            return ResponseEntity.badRequest().build();
        }
        SampleDataset.deleteEmployeeById(id);
        final ResponseEntity<EntityModel<Employee>> responseEntity = ResponseEntity.noContent().build();
        logger.info("deleteEmployee(): employee id[{}]", id);
        return responseEntity;
    }

    /**
     * Saves the {@link Employee}.
     *
     * @param employee the {@link Employee}
     * @return the {@link ResponseEntity} with the {@link EntityModel} for the {@link Employee}
     */
    private ResponseEntity<EntityModel<Employee>> saveEmployee(Employee employee) {
        try {
            SampleDataset.saveEmployee(employee);
            final EntityModel<Employee> entityModel = assembler.toModel(employee);
            final URI uri = new URI(entityModel.getRequiredLink(IanaLinkRelations.SELF).getHref());
            return ResponseEntity.created(uri).body(entityModel);
        } catch (Exception e) {
            logger.error("saveEmployee(): exception[{}], employee id[{}]", e.getMessage(), employee.getId());
            return ResponseEntity.internalServerError().build();
        }
    }
}
