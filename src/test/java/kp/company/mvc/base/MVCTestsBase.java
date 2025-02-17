package kp.company.mvc.base;

import kp.company.TestsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The base class for tests with server-side support.
 * <p>
 * The server is <b>not started</b>.
 * </p>
 * <p>
 * These tests use the 'Spring MVC Test Framework'.
 * </p>
 * <p>
 * This framework does not use a running Servlet container.
 * </p>
 */
public abstract class MVCTestsBase extends TestsBase {
    /**
     * The {@link MockMvc}.
     */
    @Autowired
    protected MockMvc mockMvc;
}