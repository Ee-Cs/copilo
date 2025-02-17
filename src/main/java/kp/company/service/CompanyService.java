package kp.company.service;

import java.util.Collection;
import java.util.List;

import kp.company.domain.Department;
import kp.company.domain.Employee;
import kp.company.domain.Title;
import kp.company.exception.CompanyException;

/**
 * The company service.
 *
 */
public interface CompanyService {

	/**
	 * Gets the list of the {@link Title}s.
	 * 
	 * @return the unmodifiable list of the {@link Title}s
	 */
	static List<Title> getTitleList() {
		return List.of(Title.ANALYST, Title.DEVELOPER, Title.MANAGER);
	}

	/**
	 * Reloads the company service data.
	 * 
	 */
	void reloadData();

	/**
	 * Gets the {@link Collection} of the {@link Department}s.
	 * 
	 * @return the {@link Collection} of the {@link Department}s
	 */
	Collection<Department> getDepartmentCollection();

	/**
	 * Finds the {@link Department} by id.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @return the {@link Department}
	 * @throws CompanyException the {@link CompanyException}
	 */
	Department findDepartment(long departmentId) throws CompanyException;

	/**
	 * Creates the {@link Department}.
	 * 
	 * @return the {@link Department}
	 */
	Department createDepartment();

	/**
	 * Saves the {@link Department}.
	 * 
	 * @param department the {@link Department}
	 */
	void saveDepartment(Department department);

	/**
	 * Deletes the {@link Department} by id.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @throws CompanyException the {@link CompanyException}
	 */
	void deleteDepartment(long departmentId) throws CompanyException;

	/**
	 * Finds the {@link Employee} by the id of the {@link Department} and the id of
	 * the {@link Employee}.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @param employeeId   the id of the {@link Employee}
	 * @return the {@link Employee}
	 * @throws CompanyException the {@link CompanyException}
	 */
	Employee findEmployee(long departmentId, long employeeId) throws CompanyException;

	/**
	 * Creates the {@link Employee} by the department id.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @return the new {@link Employee}
	 * @throws CompanyException the {@link CompanyException}
	 */
	Employee createEmployee(long departmentId) throws CompanyException;

	/**
	 * Saves the {@link Employee}.
	 * 
	 * @param employee the {@link Employee}
	 * @throws CompanyException the {@link CompanyException}
	 */
	void saveEmployee(Employee employee) throws CompanyException;

	/**
	 * Deletes the {@link Employee} by the id of the {@link Department} and the id
	 * of the {@link Employee}.
	 * 
	 * @param departmentId the id of the {@link Department}
	 * @param employeeId   the id of the {@link Employee}
	 * @throws CompanyException the {@link CompanyException}
	 */
	void deleteEmployee(long departmentId, long employeeId) throws CompanyException;
}