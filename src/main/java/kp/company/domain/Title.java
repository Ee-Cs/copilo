package kp.company.domain;

/**
 * Job title of the employee.
 * 
 */
public enum Title {
	/**
	 * Analyst
	 */
	ANALYST("Analyst"),
	/**
	 * Developer
	 */
	DEVELOPER("Developer"),
	/**
	 * Manager
	 */
	MANAGER("Manager");

	private final String name;

	/**
	 * Parameterized constructor.
	 * 
	 * @param name the name
	 */
	Title(String name) {
		this.name = name;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
}