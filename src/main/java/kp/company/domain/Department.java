package kp.company.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import kp.Constants;

/**
 * Simple JavaBean domain object representing a department.
 *
 */
@Entity
public class Department {

	@Id
	private long id;

	private String name;

	@JsonIgnoreProperties("department")
	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Employee> employees = new ArrayList<>();

	/**
	 * Constructor.
	 *
	 */
	public Department() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param id   the id
	 * @param name the name
	 */
	public Department(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	/**
	 * Creates the department from the index.
	 * 
	 * @param depIndex the department index
	 * @return the department
	 */
	public static Department fromIndex(long depIndex) {
		return new Department(depIndex, Constants.DEP_NAME_FUN.apply(depIndex));
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
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the {@link Employee}s.
	 * 
	 * @return the list of the {@link Employee}s
	 */
	public List<Employee> getEmployees() {
		return employees;
	}

	/**
	 * Sets the {@link Employee}s.
	 * 
	 * @param employees the list of the {@link Employee}s
	 */
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
}
