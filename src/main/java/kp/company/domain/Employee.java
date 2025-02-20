package kp.company.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Simple JavaBean domain object representing an employee.
 * 
 */
public class Employee {

	private long id;

	@NotBlank
	@Size(min = 1, max = 20)
	private String firstName;

	@NotBlank
	@Size(min = 1, max = 20)
	private String lastName;

	private Title title = Title.ANALYST;

	private long departmentId;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id the id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the first name.
	 * 
	 * @return the first name
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Sets the first name.
	 * 
	 * @param firstName the first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 * 
	 * @return the last name
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Sets the last name.
	 * 
	 * @param lastName the last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the {@link Title}.
	 * 
	 * @return the {@link Title}
	 */
	public Title getTitle() {
		return title;
	}

	/**
	 * Sets the {@link Title}.
	 * 
	 * @param title the {@link Title} to set
	 */
	public void setTitle(Title title) {
		this.title = title;
	}

	/**
	 * Gets the department id.
	 * 
	 * @return the id of the department
	 */
	public long getDepartmentId() {
		return departmentId;
	}

	/**
	 * Sets the department id.
	 * 
	 * @param departmentId the id of the department to set
	 */
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}
}