package kp;

import org.apache.http.HttpHost;

import java.io.File;

/**
 * The constants.
 */
@SuppressWarnings("doclint:missing")
public final class Constants {
    /**
     * The sleep time after the dataset import.
     */
    public static final int WAIT_AFTER_DATASET_IMPORT = 5;
    /**
     * The login
     */
    public static final String LOGIN = "elastic";
    /**
     * The certificate file
     */
    public static final File CERTIFICATE_FILE = new File("src/main/resources/http_ca.crt");
    /**
     * The dataset file created in Solr project 'Study15'
     */
    public static final File DATASET_FILE = new File("../Study15/solr-requests/dataset.json");
    /**
     * The HTTP host
     */
    public static final HttpHost HTTPS_HOST = new HttpHost("localhost", 9200, "https");
    /**
     * The index name
     */
    public static final String INDEX_NAME = "kp_study";

    /**
     * Private constructor to prevent instantiation.
     */
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}
