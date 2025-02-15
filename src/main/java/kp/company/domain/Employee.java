package kp.company.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Simple JavaBean domain object representing an employee.
 * <p>
 * The 'equals()' and 'hashCode()' methods are overridden because instances of the subclasses are in Sets.
 * </p>
 */
@Entity
public class Employee {

	@Id
	private long id;

	private String firstName;

	private String lastName;

	@JsonIgnoreProperties("employees")
	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;

	/**
	 * Constructor.
	 *
	 */
	public Employee() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param id        the id
	 * @param firstName the first name
	 * @param lastName  the last name
	 */
	public Employee(long id, String firstName, String lastName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

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
	 * @param firstName the first name
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
	 * @param lastName the last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the {@link Department}.
	 * 
	 * @return the {@link Department}
	 */
	public Department getDepartment() {
		return this.department;
	}

	/**
	 * Sets the {@link Department}.
	 * 
	 * @param department the {@link Department}
	 */
	public void setDepartment(Department department) {
		this.department = department;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Employee other)) {
			return false;
		}
		return id == other.id;
	}

}
