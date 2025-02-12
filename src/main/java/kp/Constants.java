package kp;

import java.util.List;

/**
 * The constants.
 *
 */
public final class Constants {
	/**
	 * The bootstrap server endpoint. Host name: 'ptc-kafka' or 'localhost'.
	 */
	public static final String BOOTSTRAP_SERVER = "ptc-kafka:9092";
	/**
	 * The first producer topic.
	 */
	public static final String TOPIC_PROD_1 = "prod-1";
	/**
	 * The second producer topic.
	 */
	public static final String TOPIC_PROD_2 = "prod-2";
	/**
	 * The first consumer topic.
	 */
	public static final String TOPIC_CONS_1 = "cons-1";
	/**
	 * The second consumer topic.
	 */
	public static final String TOPIC_CONS_2 = "cons-2";
	/**
	 * The list of keys - first set.
	 */
	public static final List<String> KEY_LIST_1 = List.of("key-1", "key-2", "key-3", "key-4");
	/**
	 * The list of values for producer - first set.
	 */
	public static final List<String> VAL_LIST_P1 = List.of("a", "b", "c", "d");
	/**
	 * The list of keys - second set.
	 */
	public static final List<String> KEY_LIST_2 = List.of("key-5", "key-6", "key-7", "key-8");
	/**
	 * The list of values for producer - second set.
	 */
	public static final List<String> VAL_LIST_P2 = List.of("A", "B", "C", "D");
	/**
	 * The list of values for consumer - first set.
	 */
	public static final List<String> VAL_LIST_C1 = List.of("a", "c");
	/**
	 * The list of values for consumer - second set.
	 */
	public static final List<String> VAL_LIST_C2 = List.of("b", "d");
	/**
	 * The thick line.
	 */
	public static final String THICK_LINE = "=".repeat(50);
	/**
	 * The thin line.
	 */
	public static final String THIN_LINE = "-".repeat(50);
	/**
	 * The line separator.
	 */
	public static final String LINE_SEP = System.lineSeparator();

	/**
	 * Private constructor to prevent instantiation.
	 */
	private Constants() {
		throw new IllegalStateException("Utility class");
	}
}