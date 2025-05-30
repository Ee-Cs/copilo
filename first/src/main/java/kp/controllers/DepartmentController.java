package kp.controllers;

import kp.models.DepartmentForResponse;
import kp.services.clients.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * REST controller for department-related queries.
 * <p>
 * Handles retrieval of department lists and individual departments by their id.
 * </p>
 */
@RestController
@RequestMapping("/departments")
public class DepartmentController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final DepartmentService departmentService;

    /**
     * Parameterized constructor.
     *
     * @param departmentService the service used for department operations
     */
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Retrieves a list of departments.
     *
     * @return a ResponseEntity containing the list of departments, or 404 if none found
     */
    @GetMapping
    public ResponseEntity<List<DepartmentForResponse>> getDepartments() {

        final ResponseEntity<List<DepartmentForResponse>> responseEntity = departmentService.getDepartments()
                .map(list -> list.stream().map(DepartmentForResponse::new).toList())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        logger.info("getDepartments():");
        return responseEntity;
    }

    /**
     * Retrieves a department by its id.
     *
     * @param id the id of the department to retrieve
     * @return a ResponseEntity containing the department, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentForResponse> getDepartmentById(@PathVariable("id") String id) {

        final ResponseEntity<DepartmentForResponse> responseEntity =
                departmentService.getDepartmentById(Integer.parseInt(id))
                        .map(DepartmentForResponse::new)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        logger.info("getDepartmentById(): id[{}]", id);
        return responseEntity;
    }
}