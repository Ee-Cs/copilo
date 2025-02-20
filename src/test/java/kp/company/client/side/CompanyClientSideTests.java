package kp.company.client.side;

import kp.company.client.side.base.ClientSideTestsBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Client-side tests for the company.
 */
class CompanyClientSideTests extends ClientSideTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Test to verify forwarding from root to the index page.
     */
    @Test
    void shouldForwardFromRootToIndexPage() {
        // GIVEN
        final String requestUrl = "http://localhost:%d/".formatted(port);
        // WHEN
        final String result = restTemplate.getForObject(requestUrl, String.class);
        // THEN
        Assertions.assertThat(result).contains("<meta http-equiv=\"Refresh\" content=\"0; URL=/company\">");
        logger.info("shouldForwardFromRootToIndexPage():");
    }

    /**
     * Test to verify retrieving the home page.
     */
    @Test
    void shouldGetHomePage() {
        // GIVEN
        final String requestUrl = "http://localhost:%d/company".formatted(port);
        // WHEN
        final String result = restTemplate.getForObject(requestUrl, String.class);
        // THEN
        Assertions.assertThat(result).contains(accessor.getMessage("company"))
                .contains(accessor.getMessage("departments"));
        logger.info("shouldGetHomePage():");
    }
}