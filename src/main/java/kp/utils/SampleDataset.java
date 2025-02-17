package kp.utils;

import static kp.Constants.DEP_INDEX_LOWER_BOUND;
import static kp.Constants.DEP_INDEX_UPPER_BOUND;
import static kp.Constants.DEP_NAME_FUN;
import static kp.Constants.EMP_F_NAME_FUN;
import static kp.Constants.EMP_INDEX_LOWER_BOUND;
import static kp.Constants.EMP_INDEX_UPPER_BOUND;
import static kp.Constants.EMP_L_NAME_FUN;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

import kp.company.domain.Department;
import kp.company.domain.Employee;
import kp.company.domain.Title;
import kp.company.service.CompanyService;

/**
 * The sample dataset.
 * <p>
 * The standard dataset:
 * </p>
 * <ol>
 *  <li>Department
 *   <ol>
 *    <li>Employee</li>
 *    <li>Employee</li>
 *    <li>Employee</li>
 *   </ol>
 *  </li>
 * <li>Department
 *  <ol>
 *   <li>Employee</li>
 *   <li>Employee</li>
 *   <li>Employee</li>
 *  </ol>
 *  </li>
 * </ol>
 */
public class SampleDataset {

	private static Map<Long, Department> departmentMap;

	private static AtomicLong departmentIdSequence;

	private static AtomicLong employeeIdSequence;

	/**
	 * Private constructor to prevent instantiation.
	 * 
	 */
	private SampleDataset() {
	}

	/**
	 * Creates the data for the {@link Department}s with the {@link Employee}s.
	 * 
	 * @param departmentMapParam        the {@link Department}'s map
	 * @param departmentIdSequenceParam the {@link Department} id's sequence
	 * @param employeeIdSequenceParam   the {@link Employee} id's sequence
	 */
	public static void generateDepartments(Map<Long, Department> departmentMapParam,
			AtomicLong departmentIdSequenceParam, AtomicLong employeeIdSequenceParam) {

		departmentMap = departmentMapParam;
		departmentIdSequence = departmentIdSequenceParam;
		employeeIdSequence = employeeIdSequenceParam;

		departmentMap.clear();
		departmentIdSequence.set(DEP_INDEX_LOWER_BOUND);
		employeeIdSequence.set(EMP_INDEX_LOWER_BOUND);
		LongStream.rangeClosed(DEP_INDEX_LOWER_BOUND, DEP_INDEX_UPPER_BOUND).boxed()
				.forEach(_ -> generateDepartment());
	}

	/**
	 * Generates the {@link Department}.
	 * 
	 */
	private static void generateDepartment() {

		final long departmentId = departmentIdSequence.getAndIncrement();
		final Department department = new Department();
		department.setId(departmentId);
		department.setName(DEP_NAME_FUN.apply(departmentId));
		LongStream.rangeClosed(EMP_INDEX_LOWER_BOUND, EMP_INDEX_UPPER_BOUND).boxed()
				.forEach(_ -> generateEmployee(department));
		departmentMap.put(departmentId, department);
	}

	/**
	 * Generates the {@link Employee}.
	 * 
	 * @param department the {@link Department}
	 */
	private static void generateEmployee(Department department) {

		final long employeeId = employeeIdSequence.getAndIncrement();
		final Employee employee = new Employee();
		employee.setId(employeeId);
		employee.setFirstName(EMP_F_NAME_FUN.apply(employeeId));
		employee.setLastName(EMP_L_NAME_FUN.apply(employeeId));
		final List<Title> titleList = CompanyService.getTitleList();
		final int index = ((int) employeeId - 1) % 3;
		employee.setTitle(titleList.get(index));
		employee.setDepartmentId(department.getId());
		department.putEmployee(employee);
	}
}