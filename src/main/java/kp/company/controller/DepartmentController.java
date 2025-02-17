package kp.company.controller;

import jakarta.validation.Valid;
import kp.company.domain.Department;
import kp.company.exception.CompanyException;
import kp.company.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static kp.Constants.*;

/**
 * The web controller for the {@link Department}.
 */
@Controller
public class DepartmentController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final CompanyService companyService;

    /**
     * Parameterized constructor.
     *
     * @param companyService the {@link CompanyService}
     */
    public DepartmentController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Gets the list of the {@link Department}s.
     *
     * @param model the {@link Model}
     * @return the view name
     */
    @GetMapping("/listDepartments")
    public String listDepartments(Model model) {

        model.addAttribute("departmentCollection", companyService.getDepartmentCollection());
        logger.info("listDepartments():");
        return DEPARTMENTS_LIST_VIEW_NAME;
    }

    /**
     * Starts the adding of the {@link Department}.
     *
     * @param model the {@link Model}
     * @return the view name
     */
    @GetMapping("/startDepartmentAdding")
    public String startDepartmentAdding(Model model) {

        final Department department = companyService.createDepartment();
        model.addAttribute(DEPARTMENT_ATTR_NAME, department);
        logger.info("startDepartmentAdding():");
        return DEPARTMENTS_EDIT_VIEW_NAME;
    }

    /**
     * Starts the editing of the {@link Department}.
     *
     * @param departmentId the id of the {@link Department}
     * @param model        the {@link Model}
     * @return the view name
     */
    @GetMapping("/startDepartmentEditing")
    public String startDepartmentEditing(long departmentId, Model model) {

        Department department;
        try {
            department = companyService.findDepartment(departmentId);
        } catch (CompanyException ex) {
            logger.error("startDepartmentEditing(): CompanyException[{}], department id[{}]",
                    ex.getMessage(), departmentId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_NOT_FOUND_MSG, ex);
        }
        model.addAttribute(DEPARTMENT_ATTR_NAME, department);
        logger.info("startDepartmentEditing(): department id[{}]", department.getId());
        return DEPARTMENTS_EDIT_VIEW_NAME;
    }

    /**
     * Saves the {@link Department}.
     *
     * @param department    the {@link Department}
     * @param bindingResult the {@link BindingResult}
     * @return the view name
     */
    @PostMapping(value = "/finishDepartmentEditing", params = {"save"})
    public String saveDepartment(@Valid Department department, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final List<String> messagesList = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                    .toList();
            logger.info("saveDepartment(): error messages{}", messagesList);
            return DEPARTMENTS_EDIT_VIEW_NAME;
        }
        companyService.saveDepartment(department);
        logger.info("saveDepartment(): department id[{}]", department.getId());
        return REDIRECT_LIST_DEPARTMENTS_VIEW_NAME;
    }

    /**
     * Cancels the editing of the {@link Department}.
     *
     * @param departmentId the id of the {@link Department}
     * @return the view name
     */
    @PostMapping(value = "/finishDepartmentEditing", params = {"cancel"})
    public String cancelDepartmentEditing(@RequestParam("id") long departmentId) {

        logger.info("cancelDepartmentEditing(): department id[{}]", departmentId);
        return REDIRECT_LIST_DEPARTMENTS_VIEW_NAME;
    }

    /**
     * Starts the deleting of the {@link Department}.
     *
     * @param departmentId the id of the {@link Department}
     * @param model        the {@link Model}
     * @return the view name
     */
    @GetMapping("/startDepartmentDeleting")
    public String startDepartmentDeleting(long departmentId, Model model) {

        Department department;
        try {
            department = companyService.findDepartment(departmentId);
        } catch (CompanyException ex) {
            logger.error("startDepartmentDeleting(): CompanyException[{}], department id[{}]",
                    ex.getMessage(), departmentId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_NOT_FOUND_MSG, ex);
        }
        model.addAttribute(DEPARTMENT_ATTR_NAME, department);
        logger.info("startDepartmentDeleting(): department id[{}]", departmentId);
        return DEPARTMENTS_CONFIRM_DELETE_VIEW_NAME;
    }

    /**
     * Deletes the {@link Department}.
     *
     * @param departmentId the id of the {@link Department}
     * @return the view name
     */
    @PostMapping(value = "/finishDepartmentDeleting", params = {"delete"})
    public String deleteDepartment(@RequestParam("id") long departmentId) {

        try {
            companyService.deleteDepartment(departmentId);
        } catch (CompanyException ex) {
            logger.error("deleteDepartment(): CompanyException[{}], department id[{}]", ex.getMessage(), departmentId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, DEP_NOT_FOUND_MSG, ex);
        }
        logger.info("deleteDepartment(): department id[{}]", departmentId);
        return REDIRECT_LIST_DEPARTMENTS_VIEW_NAME;
    }

    /**
     * Cancels the deleting of the {@link Department}.
     *
     * @param departmentId the id of the {@link Department}
     * @return the view name
     */
    @PostMapping(value = "/finishDepartmentDeleting", params = {"cancel"})
    public String cancelDepartmentDeleting(@RequestParam("id") long departmentId) {

        logger.info("cancelDepartmentDeleting(): department id[{}]", departmentId);
        return REDIRECT_LIST_DEPARTMENTS_VIEW_NAME;
    }
}
