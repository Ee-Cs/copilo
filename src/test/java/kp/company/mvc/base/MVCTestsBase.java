package kp.company.mvc.base;

import kp.company.base.TestsBase;
import kp.company.controller.DepartmentController;
import kp.company.controller.EmployeeController;
import kp.company.service.CompanyService;
import kp.company.service.CompanyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The base class for tests with server-side support.
 */
public abstract class MVCTestsBase  extends TestsBase {
    /**
     * {@link MockMvc}.
     */
    @Autowired
    protected MockMvc mockMvc;
}