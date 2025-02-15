package kp.company.repository;

import java.util.List;

import kp.company.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import kp.company.domain.Employee;

/**
 * The {@link Employee}'s repository. The extension of {@link JpaRepository}.
 * 
 * @see org.springframework.data.repository.PagingAndSortingRepository
 * @see org.springframework.data.repository.CrudRepository
 * @see org.springframework.data.repository.Repository
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	/**
	 * Finds the {@link Employee}s by first name and last name.
	 * 
	 * @param firstName the {@link Employee}'s first name
	 * @param lastName  the {@link Employee}'s last name
	 * @return the list of the {@link Employee}s
	 */
	List<Employee> findByFirstNameAndLastNameOrderByLastName(@Param("firstName") String firstName,
			@Param("lastName") String lastName);

	/**
	 * Finds the {@link Employee}s by the first name and the last name using
	 * <code>LIKE</code> patterns.
	 * 
	 * @param firstName the {@link Employee}'s first name
	 * @param lastName  the {@link Employee}'s last name
	 * @return the list of the {@link Employee}s
	 */
	List<Employee> findByFirstNameLikeAndLastNameLikeOrderByLastName(@Param("firstName") String firstName,
			@Param("lastName") String lastName);

	/**
	 * Finds the {@link Employee}s by the {@link Department}'s name.<br/>
	 * The result is sorted descending by the {@link Employee}'s last name.
	 * 
	 * @param name the {@link Department}'s name
	 * @return the list of the {@link Employee}s
	 */
	List<Employee> findByDepartmentNameOrderByLastNameDesc(@Param("name") String name);

}
