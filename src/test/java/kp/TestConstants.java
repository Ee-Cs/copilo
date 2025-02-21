package kp;

import static kp.Constants.*;

/**
 * Test constants.
 */
@SuppressWarnings("doclint:missing")
public final class TestConstants {
    public static final String ROOT_URL = "http://localhost";

    public static final long DEP_TEST_INDEX_LOWER_BOUND = 1;
    public static final long DEP_TEST_INDEX_UPPER_BOUND = 2;
    public static final long EMP_TEST_INDEX_LOWER_BOUND = 1;
    public static final long EMP_TEST_INDEX_UPPER_BOUND = 2;
    public static final int DEP_TEST_SIZE = Long.valueOf(DEP_TEST_INDEX_UPPER_BOUND - DEP_TEST_INDEX_LOWER_BOUND + 1)
            .intValue();
    public static final int EMP_TEST_SIZE = Long.valueOf(EMP_TEST_INDEX_UPPER_BOUND - EMP_TEST_INDEX_LOWER_BOUND + 1)
            .intValue();

    public static final long EXPECTED_DEPARTMENT_ID = DEP_TEST_INDEX_LOWER_BOUND;
    public static final String EXPECTED_DEPARTMENT_NAME = DEP_NAME_FUN.apply(DEP_TEST_INDEX_LOWER_BOUND);

    public static final long EXPECTED_EMPLOYEE_ID = EMP_INDEX_FUN.applyAsLong(DEP_TEST_INDEX_LOWER_BOUND,
            EMP_TEST_INDEX_LOWER_BOUND);
    public static final String EXPECTED_EMPLOYEE_F_NAME = EMP_F_NAME_FUN.apply(DEP_TEST_INDEX_LOWER_BOUND,
            EMP_TEST_INDEX_LOWER_BOUND);
    public static final String EXPECTED_EMPLOYEE_L_NAME = EMP_L_NAME_FUN.apply(DEP_TEST_INDEX_LOWER_BOUND,
            EMP_TEST_INDEX_LOWER_BOUND);

    public static final long CREATED_DEPARTMENT_ID = 12345;
    public static final String CREATED_DEPARTMENT_NAME = DEP_NAME_FUN.apply(CREATED_DEPARTMENT_ID);
    public static final String CREATED_DEPARTMENT_CONTENT = "{\"id\":%d, \"name\":\"%s\"}".formatted(
            CREATED_DEPARTMENT_ID, CREATED_DEPARTMENT_NAME);
    public static final String UPDATED_DEPARTMENT_NAME = "D-Name-UPDATED";
    public static final String UPDATED_DEPARTMENT_CONTENT = "{\"name\":\"%s\"}".formatted(UPDATED_DEPARTMENT_NAME);
    public static final long ABSENT_DEPARTMENT_ID = 99999;

    public static final long CREATED_EMPLOYEE_ID = 67890;
    public static final String CREATED_EMPLOYEE_F_NAME = EMP_F_NAME_FUN.apply(CREATED_DEPARTMENT_ID,
            CREATED_EMPLOYEE_ID);
    public static final String CREATED_EMPLOYEE_L_NAME = EMP_L_NAME_FUN.apply(CREATED_DEPARTMENT_ID,
            CREATED_EMPLOYEE_ID);
    public static final String CREATED_EMPLOYEE_CONTENT =
            "{\"id\":%d,\"firstName\":\"%s\",\"lastName\":\"%s\",\"department\":{\"id\":%d}}".formatted(
                    CREATED_EMPLOYEE_ID, CREATED_EMPLOYEE_F_NAME, CREATED_EMPLOYEE_L_NAME, CREATED_DEPARTMENT_ID);
    public static final String UPDATED_EMPLOYEE_F_NAME = "EF-Name-UPDATED";
    public static final String UPDATED_EMPLOYEE_L_NAME = "EL-Name-UPDATED";
    public static final String UPDATED_EMPLOYEE_CONTENT =
            "{\"firstName\":\"%s\",\"lastName\":\"%s\",\"department\":{\"id\":%d}}".formatted(
                    UPDATED_EMPLOYEE_F_NAME, UPDATED_EMPLOYEE_L_NAME, DEP_TEST_INDEX_LOWER_BOUND);
    public static final long ABSENT_EMPLOYEE_ID = 99999;

    public static final String FIND_DEP_HREF = "%s%s%s".formatted(ROOT_URL, COMPANY_PATH, DEPARTMENTS_PATH);
    public static final String FIND_EMP_HREF = "%s%s%s".formatted(ROOT_URL, COMPANY_PATH, EMPLOYEES_PATH);
    public static final String FIND_EMP_BY_ID_HREF = "%s/%d".formatted(FIND_EMP_HREF, EXPECTED_EMPLOYEE_ID);

    private TestConstants() {
        throw new IllegalStateException("Utility class");
    }
}