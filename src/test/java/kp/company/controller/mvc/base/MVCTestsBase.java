package kp.company.controller.mvc.base;


import kp.company.TestDatasetLoader;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The base class for tests with server-side support.
 */
@SpringBootTest
@AutoConfigureMockMvc
public abstract class MVCTestsBase {
    /**
     * {@link MockMvc}.
     */
    @Autowired
    protected MockMvc mockMvc;

    /**
     * Flag for output printing.
     */
    protected static final boolean VERBOSE = false;

    /**
     * Deletes test dataset.
     */
    @AfterEach
    void deleteTestDataset() {
        TestDatasetLoader.deleteTestDataset();
    }

}