package kp.company.controller.client.side.base;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

/**
 * The base class for client-side tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ClientSideTestsBase {
    /**
     * {@link TestRestTemplate} for making REST calls in tests.
     */
    @Autowired
    protected TestRestTemplate restTemplate;
}