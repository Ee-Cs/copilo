package kp;

import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

/**
 * Test constants.
 */
@SuppressWarnings("doclint:missing")
public final class TestConstants {
    public static final VariableMap VARIABLES_HIGH =
            Variables.putValue(Constants.IMPACT_KEY, "medium").putValue(Constants.URGENCY_KEY, "high");
    public static final VariableMap VARIABLES_MEDIUM =
            Variables.putValue(Constants.IMPACT_KEY, "medium").putValue(Constants.URGENCY_KEY, "medium");
    public static final VariableMap VARIABLES_LOW =
            Variables.putValue(Constants.IMPACT_KEY, "low").putValue(Constants.URGENCY_KEY, "medium");

    public static final long PRIORITY_HIGH = 80;
    public static final long PRIORITY_MEDIUM = 50;
    public static final long PRIORITY_LOW = 20;

    private TestConstants() {
        throw new IllegalStateException("Utility class");
    }
}
