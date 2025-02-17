package kp;

import static kp.Constants.DEP_INDEX_LOWER_BOUND;
import static kp.Constants.DEP_NAME_FUN;
import static kp.Constants.EMP_F_NAME_FUN;
import static kp.Constants.EMP_INDEX_LOWER_BOUND;
import static kp.Constants.EMP_L_NAME_FUN;

/**
 * Test constants.
 *
 */
@SuppressWarnings("doclint:missing")
public final class TestConstants {
	public static final String ABSENT_ID = "999";

	public static final String TEST_DEPARTMENT_ID_PARAM = Long.valueOf(DEP_INDEX_LOWER_BOUND).toString();
	public static final String EXPECTED_DEPARTMENT_NAME = DEP_NAME_FUN.apply(DEP_INDEX_LOWER_BOUND);
	public static final String TEST_EMPLOYEE_ID_PARAM = Long.valueOf(EMP_INDEX_LOWER_BOUND).toString();
	public static final String EXPECTED_EMPLOYEE_FIRST_NAME = EMP_F_NAME_FUN.apply(EMP_INDEX_LOWER_BOUND);
	public static final String EXPECTED_EMPLOYEE_LAST_NAME = EMP_L_NAME_FUN.apply(EMP_INDEX_LOWER_BOUND);

	public static final String CHANGED_DEPARTMENT_NAME = "D-Name-CHANGED";
	public static final String CHANGED_EMPLOYEE_FIRST_NAME = "EF-Name-CHANGED";
	public static final String CHANGED_EMPLOYEE_LAST_NAME = "EL-Name-CHANGED";

	private TestConstants() {
		throw new IllegalStateException("Utility class");
	}
}