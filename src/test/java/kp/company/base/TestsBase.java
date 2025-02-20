package kp.company.base;

import kp.company.controller.DepartmentController;
import kp.company.controller.EmployeeController;
import kp.company.service.CompanyService;
import kp.company.service.CompanyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * The base class for all tests.
 */
public abstract class TestsBase {
    /**
     * The {@link MessageSourceAccessor}.
     */
    protected MessageSourceAccessor accessor;

    @Autowired
    private MessageSource messageSource;

    private static final CompanyService companyService = new CompanyServiceImpl();

    /**
     * Initializes the {@link CompanyServiceImpl} data before every test.
     */
    @BeforeEach
    protected void initialize() {

        companyService.reloadData();
        accessor = new MessageSourceAccessor(messageSource);
    }

    /**
     * Creates a {@link DepartmentController}.
     *
     * @return the  {@link DepartmentController}.
     */
    protected static DepartmentController createDepartmentController() {
        return new DepartmentController(companyService);
    }

    /**
     * Creates a {@link EmployeeController}.
     *
     * @return the  {@link EmployeeController}.
     */
    protected static EmployeeController createEmployeeController() {
        return new EmployeeController(companyService);
    }

}
