package kp.company.client.side.base;

import kp.company.base.TestsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * The base class for client-side tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ClientSideTestsBase extends TestsBase {
    /**
     * {@link TestRestTemplate} for making REST calls in tests.
     */
    @Autowired
    protected TestRestTemplate restTemplate;
    /**
     * HTTP server port that was allocated at runtime.
     */
    @LocalServerPort
    protected int port;
}