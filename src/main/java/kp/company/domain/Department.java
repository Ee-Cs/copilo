package kp.company.domain;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Simple JavaBean domain object representing a department.
 *
 */
public class Department {

	private long id;

	@NotBlank
	@Size(min = 1, max = 20)
	private String name;

	private final Map<Long, Employee> employeeMap = new TreeMap<>();

	/**
	 * The constructor.
	 */
	public Department() {
		super();
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
	 * Gets the {@link Employee} by id.
	 * 
	 * @param employeeId the id of the {@link Employee}
	 * @return the {@link Employee}
	 */
	public Employee getEmployee(long employeeId) {
		return employeeMap.get(employeeId);
	}

	/**
	 * Puts the {@link Employee}.
	 * 
	 * @param employee the {@link Employee} to put
	 */
	public void putEmployee(Employee employee) {
		employeeMap.put(employee.getId(), employee);
	}

	/**
	 * Removes the {@link Employee} by id.
	 * 
	 * @param employeeId the id of the {@link Employee}
	 */
	public void removeEmployee(long employeeId) {
		employeeMap.remove(employeeId);
	}

	/**
	 * Gets the {@link Collection} of the {@link Employee}s.
	 * 
	 * @return the {@link Collection} of the {@link Employee}s
	 */
	public Collection<Employee> getEmployeeCollection() {
		return employeeMap.values();
	}
}