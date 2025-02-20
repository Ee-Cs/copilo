package kp.company.client.side;

import kp.company.client.side.base.ClientSideTestsBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Client side tests for company.
 */
class CompanyClientSideTests extends ClientSideTestsBase {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Should forward from root to index page.
     */
    @Test
    void shouldForwardFromRootToIndexPage() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/", port);
        // WHEN
        final String result = restTemplate.getForObject(requestUrl, String.class);
        // THEN
        Assertions.assertThat(result).contains("<meta http-equiv=\"Refresh\" content=\"0; URL=/company\">");
        logger.info("shouldForwardFromRootToIndexPage():");
    }

    /**
     * Should get company.
     */
    @Test
    void shouldGetHomePage() {
        // GIVEN
        final String requestUrl = String.format("http://localhost:%s/company", port);
        // WHEN
        final String result = restTemplate.getForObject(requestUrl, String.class);
        // THEN
        Assertions.assertThat(result).contains(accessor.getMessage("company"))
                .contains(accessor.getMessage("departments"));
        logger.info("shouldGetHomePage():");
    }
}