package kp.company.controller;

import kp.Constants;
import kp.SampleDataset;
import kp.company.assembler.DepartmentAssembler;
import kp.company.domain.Department;
import kp.company.domain.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Optional;

/**
 * The HATEOAS RESTful web service controller for handling {@link Department} entities.
 */
@RestController
@RequestMapping(Constants.COMPANY_PATH)
public class DepartmentController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DepartmentAssembler assembler;

    /**
     * Parameterized constructor.
     *
     * @param assembler the {@link DepartmentAssembler}
     */
    public DepartmentController(DepartmentAssembler assembler) {
        this.assembler = assembler;
    }

    /**
     * Creates a new {@link Department}.
     *
     * @param department the {@link Department}
     * @return the {@link ResponseEntity} with the {@link EntityModel} for the {@link Department}
     */
    @PostMapping(Constants.DEPARTMENTS_PATH)
    public ResponseEntity<EntityModel<Department>> createDepartment(@RequestBody Department department) {

        if (Optional.ofNullable(department).isEmpty()) {
            logger.error("createDepartment(): bad request - empty request body");
            return ResponseEntity.badRequest().build();
        }
        final ResponseEntity<EntityModel<Department>> responseEntity = saveDepartment(department);
        logger.info("createDepartment():");
        return responseEntity;
    }

    /**
     * Retrieves the {@link Department} with the specified id.
     *
     * @param id the {@link Department} id
     * @return the {@link ResponseEntity} with the {@link EntityModel} for the {@link Department}
     */
    @GetMapping(Constants.DEPARTMENT_ID_PATH)
    public ResponseEntity<EntityModel<Department>> findDepartmentById(@PathVariable("id") Long id) {

        if (Optional.ofNullable(id).isEmpty()) {
            logger.error("findDepartmentById(): bad request - empty id parameter");
            return ResponseEntity.badRequest().build();
        }
        final ResponseEntity<EntityModel<Department>> responseEntity = SampleDataset.findDepartmentById(id)
                .map(assembler::toModel).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        logger.info("findDepartmentById(): department id[{}]", id);
        return responseEntity;
    }

    /**
     * Retrieves all {@link Department}s.
     *
     * @return the {@link ResponseEntity} with the {@link CollectionModel}
     */
    @GetMapping(Constants.DEPARTMENTS_PATH)
    public ResponseEntity<CollectionModel<EntityModel<Department>>> findAllDepartments() {

        final ResponseEntity<CollectionModel<EntityModel<Department>>> responseEntity = SampleDataset.findAllDepartments()
                .map(assembler::toCollectionModel).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        logger.info("findAllDepartments():");
        return responseEntity;
    }

    /**
     * Updates the {@link Department} with the specified id.
     *
     * @param department the {@link Department}
     * @param id         the {@link Department} id
     * @return the {@link ResponseEntity} with the {@link EntityModel} for the {@link Department}
     */
    @PatchMapping(Constants.DEPARTMENT_ID_PATH)
    public ResponseEntity<EntityModel<Department>> updateDepartment(@RequestBody Department department,
                                                                    @PathVariable("id") Long id) {

        final Optional<Department> departmentOpt = Optional.ofNullable(department);
        if (departmentOpt.isEmpty() || Optional.ofNullable(id).isEmpty()) {
            logger.error("updateDepartment(): bad request - empty request body or empty parameter");
            return ResponseEntity.badRequest().build();
        }
        final ResponseEntity<EntityModel<Department>> responseEntity = SampleDataset.findDepartmentById(id)
                .map(dep -> new Department(id,
                        departmentOpt.map(Department::getName).orElse(dep.getName()),
                        dep.getEmployees()))
                .map(this::saveDepartment).orElseGet(() -> ResponseEntity.notFound().build());
        logger.info("updateDepartment(): department id[{}]", id);
        return responseEntity;
    }

    /**
     * Deletes the {@link Department} with the specified id.
     *
     * @param id the {@link Department} id
     * @return the {@link ResponseEntity}
     */
    @DeleteMapping(Constants.DEPARTMENT_ID_PATH)
    public ResponseEntity<EntityModel<Department>> deleteDepartment(@PathVariable("id") Long id) {

        if (Optional.ofNullable(id).isEmpty()) {
            logger.error("deleteDepartment(): bad request - empty id parameter");
            return ResponseEntity.badRequest().build();
        }
        SampleDataset.deleteDepartmentById(id);
        final ResponseEntity<EntityModel<Department>> responseEntity = ResponseEntity.noContent().build();
        logger.info("deleteDepartment(): department id[{}]", id);
        return responseEntity;
    }

    /**
     * Saves the {@link Department}.
     *
     * @param department the {@link Department}
     * @return the {@link ResponseEntity} with the {@link EntityModel} for the {@link Department}
     */
    private ResponseEntity<EntityModel<Department>> saveDepartment(Department department) {
        try {
            SampleDataset.saveDepartment(department);
            final EntityModel<Department> entityModel = assembler.toModel(department);
            final URI uri = new URI(entityModel.getRequiredLink(IanaLinkRelations.SELF).getHref());
            return ResponseEntity.created(uri).body(entityModel);
        } catch (Exception e) {
            logger.error("saveDepartment(): exception[{}], department id[{}]", e.getMessage(), department.getId());
            return ResponseEntity.internalServerError().build();
        }
    }

}
