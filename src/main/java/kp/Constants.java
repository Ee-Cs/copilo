package kp;

/**
 * The constants.
 *
 */
@SuppressWarnings("doclint:missing")
public final class Constants {
	/**
	 * The business process diagram.
	 */
	public static final String BUSINESS_PROCESS_DIAGRAM = "classpath:diagrams/business-process-diagram-01.bpmn";
	/**
	 * The decision diagram.
	 */
	public static final String DECISION_DIAGRAM = "diagrams/decision-requirements-diagram-01.dmn";
	/**
	 * The triage decision key.
	 */
	public static final String DECISION_KEY = "TriageDecision_01";
	/**
	 * The impact key.
	 */
	public static final String IMPACT_KEY = "impact";
	/**
	 * The urgency key.
	 */
	public static final String URGENCY_KEY = "urgency";
	/**
	 * The priority key.
	 */
	public static final String PRIORITY_KEY = "priority";
	/**
	 * The issue result key.
	 */
	public static final String RESULT_KEY = "result";
	/**
	 * The issue was evaluated.
	 */
	public static final String RESULT_EVALUATED = "evaluated";
	/**
	 * The issue was solved.
	 */
	public static final String RESULT_SOLVED = "solved";
	/**
	 * The issue was approved.
	 */
	public static final String RESULT_APPROVED = "approved";

	/**
	 * Private constructor to prevent instantiation.
	 */
	private Constants() {
		throw new IllegalStateException("Utility class");
	}
}
