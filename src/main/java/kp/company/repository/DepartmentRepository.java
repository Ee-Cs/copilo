package kp.company.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import jakarta.transaction.Transactional;
import kp.company.domain.Department;
import kp.company.domain.DepartmentDto;

/**
 * The {@link Department}'s repository. The extension of {@link JpaRepository}.
 * 
 * @see org.springframework.data.repository.PagingAndSortingRepository
 * @see org.springframework.data.repository.CrudRepository
 * @see org.springframework.data.repository.Repository
 */
@RepositoryRestResource
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	/**
	 * Finds the {@link Slice} with the {@link Department}s.
	 * 
	 * @param pageable the pagination information in the {@link Pageable}
	 * @return the {@link Slice} with the {@link Department}s
	 */
	Slice<Department> findAllByOrderByName(Pageable pageable);

	/**
	 * Finds the {@link Department}s by name.
	 * 
	 * @param name the {@link Department}'s name
	 * @return the list of the {@link Department}s
	 */
	List<Department> findByName(@Param("name") String name);

	/**
	 * Counts the {@link Department}s by name.
	 * 
	 * @param name the {@link Department}'s name
	 * @return the {@link Department}s count
	 */
	long countByName(@Param("name") String name);

	/**
	 * Finds the {@link Department} by id and counts its employees.
	 * 
	 * @param id the {@link Department}'s id
	 * @return the {@link DepartmentDto}
	 */
	@Query("""
			select new kp.company.domain.DepartmentDto(d.name, size(d.employees))
			from Department d where d.id = :id
			""")
	DepartmentDto countByCustomQuery(@Param("id") long id);

	/**
	 * Deletes the {@link Department}s by name.
	 * 
	 * @param name the {@link Department}'s name
	 * @return the deleted {@link Department}s count
	 */
	@Transactional
	long deleteByName(@Param("name") String name);

}