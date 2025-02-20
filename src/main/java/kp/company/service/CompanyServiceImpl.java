package kp.company.service;

import static kp.Constants.DEP_NOT_FOUND_EXC_FUN;
import static kp.Constants.EMP_NOT_FOUND_EXC_FUN;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import kp.company.domain.Department;
import kp.company.domain.Employee;
import kp.company.exception.CompanyException;
import kp.utils.SampleDataset;

/**
 * {@link CompanyService} implementation.
 *
 */
@Service
public class CompanyServiceImpl implements CompanyService {

	private final Map<Long, Department> departmentMap = new TreeMap<>();

	private final AtomicLong departmentIdSequence = new AtomicLong(1);

	private final AtomicLong employeeIdSequence = new AtomicLong(1);

	/**
	 * Default constructor.
	 * 
	 */
	public CompanyServiceImpl() {
		reloadData();
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void reloadData() {
		SampleDataset.generateDepartments(departmentMap, departmentIdSequence, employeeIdSequence);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Collection<Department> getDepartmentCollection() {
		return departmentMap.values();
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Department findDepartment(long departmentId) throws CompanyException {

		if (!departmentMap.containsKey(departmentId)) {
			throw DEP_NOT_FOUND_EXC_FUN.apply(departmentId);
		}
		return departmentMap.get(departmentId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Department createDepartment() {

		return new Department();
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void saveDepartment(Department department) {

		if (department.getId() == 0) {
			department.setId(departmentIdSequence.getAndIncrement());
			departmentMap.put(department.getId(), department);
		} else if(Optional.ofNullable(department.getName()).map(String::trim).isPresent()) {
			Optional.of(department.getId()).map(departmentMap::get)
					.ifPresent(dep -> dep.setName(department.getName()));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void deleteDepartment(long departmentId) throws CompanyException {

		if (!departmentMap.containsKey(departmentId)) {
			throw DEP_NOT_FOUND_EXC_FUN.apply(departmentId);
		}
		departmentMap.remove(departmentId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Employee findEmployee(long departmentId, long employeeId) throws CompanyException {

		if (!departmentMap.containsKey(departmentId)) {
			throw DEP_NOT_FOUND_EXC_FUN.apply(departmentId);
		}
		if (Objects.isNull(departmentMap.get(departmentId).getEmployee(employeeId))) {
			throw EMP_NOT_FOUND_EXC_FUN.apply(employeeId);
		}
		return departmentMap.get(departmentId).getEmployee(employeeId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Employee createEmployee(long departmentId) throws CompanyException {

		if (!departmentMap.containsKey(departmentId)) {
			throw DEP_NOT_FOUND_EXC_FUN.apply(departmentId);
		}
		final Employee employee = new Employee();
		employee.setDepartmentId(departmentId);
		employee.setTitle(CompanyService.getTitleList().getFirst());
		return employee;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void saveEmployee(Employee employee) throws CompanyException {

		final long departmentId = Objects.nonNull(employee) ? employee.getDepartmentId() : -1;
		if (!departmentMap.containsKey(departmentId)) {
			throw DEP_NOT_FOUND_EXC_FUN.apply(departmentId);
		}
		if (Objects.nonNull(employee) && employee.getId() == 0) {
			employee.setId(employeeIdSequence.getAndIncrement());
		}
		departmentMap.get(departmentId).putEmployee(employee);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void deleteEmployee(long departmentId, long employeeId) throws CompanyException {

		if (!departmentMap.containsKey(departmentId)) {
			throw DEP_NOT_FOUND_EXC_FUN.apply(departmentId);
		}
		if (Objects.isNull(departmentMap.get(departmentId).getEmployee(employeeId))) {
			throw new CompanyException(String.format("Not found employee with id[%d]", employeeId));
		}
		departmentMap.get(departmentId).removeEmployee(employeeId);
	}
}