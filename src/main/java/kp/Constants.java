package kp;

import java.io.File;
import java.nio.file.Paths;
import java.util.function.BiFunction;
import java.util.function.LongBinaryOperator;
import java.util.function.LongFunction;

/**
 * The constants.
 */
@SuppressWarnings("doclint:missing")
public final class Constants {
    private static final String ROOT = "/";
    public static final String COMPANY_PATH = ROOT + "company";
    public static final String LOAD_SAMPLE_DATASET_PATH = ROOT + "loadSampleDataset";
    public static final String DEPARTMENTS_LINK_RELATION = "departments";
    public static final String DEPARTMENTS_PATH = ROOT + DEPARTMENTS_LINK_RELATION;
    public static final String DEPARTMENT_ID_PATH = ROOT + DEPARTMENTS_LINK_RELATION + "/{id}";
    public static final String EMPLOYEES_LINK_RELATION = "employees";
    public static final String EMPLOYEES_PATH = ROOT + EMPLOYEES_LINK_RELATION;
    public static final String EMPLOYEE_ID_PATH = ROOT + EMPLOYEES_LINK_RELATION + "/{id}";

    public static final long DEP_INDEX_LOWER_BOUND = 1;
    public static final long DEP_INDEX_UPPER_BOUND = 2;
    public static final long EMP_INDEX_LOWER_BOUND = 1;
    public static final long EMP_INDEX_UPPER_BOUND = 2;
    public static final File SAMPLE_DATASET_FILE = Paths.get(System.getProperty("java.io.tmpdir"))
            .resolve("sampleDataset_Study09.json").toFile();
    public static final String LOAD_SAMPLE_DATASET_RESULT = "The sample dataset was loaded with success.";
    public static final LongFunction<String> DEP_NAME_FUN = "D-Name-%02d"::formatted;
    public static final LongBinaryOperator EMP_INDEX_FUN = (depIndex, empIndex) -> 100 * depIndex + empIndex;
    public static final BiFunction<Long, Long, String> EMP_F_NAME_FUN = (depIndex, empIndex) -> String
            .format("EF-Name-%d", EMP_INDEX_FUN.applyAsLong(depIndex, empIndex));
    public static final BiFunction<Long, Long, String> EMP_L_NAME_FUN = (depIndex, empIndex) -> String
            .format("EL-Name-%d", EMP_INDEX_FUN.applyAsLong(depIndex, empIndex));

    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}
