package kp.company.mvc;

import kp.company.controller.CompanyController;
import kp.company.mvc.base.MVCTestsBase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.invoke.MethodHandles;

/**
 * Application tests for a company with server-side support.
 */
@WebMvcTest(CompanyController.class)
class CompanyMVCTests extends MVCTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Should forward from root to index page.
     *
     * @throws Exception the {@link Exception}
     */
    @Test
    void shouldForwardFromRootToIndexPage() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/");
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.forwardedUrl("index.html"));
        logger.info("shouldForwardFromRootToIndexPage():");
    }

    /**
     * Should get company.
     *
     * @throws Exception the {@link Exception}
     */
    @Test
    void shouldGetHomePage() throws Exception {
        // GIVEN
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/company");
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("company"))));
        resultActions.andExpect(
                MockMvcResultMatchers.content().string(Matchers.containsString(accessor.getMessage("departments"))));
        logger.info("shouldGetHomePage():");
    }
}