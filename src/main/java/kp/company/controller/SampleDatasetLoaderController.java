package kp.company.controller;

import kp.company.domain.Department;
import kp.company.domain.Employee;
import kp.company.repository.DepartmentRepository;
import kp.company.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import static kp.Constants.*;

/**
 * The controller for loading the sample dataset.
 * <p>
 * The standard dataset:
 * </p>
 * <ol>
 *  <li>Department
 *   <ol>
 *    <li>Employee
 *    <li>Employee
 *   </ol>
 *  </li>
 *  <li>Department
 *   <ol>
 *    <li>Employee
 *    <li>Employee
 *   </ol>
 *  </li>
 * </ol>
 */
@RestController
public class SampleDatasetLoaderController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    private long depIndexLowerBound = DEP_INDEX_LOWER_BOUND;
    private long depIndexUpperBound = DEP_INDEX_UPPER_BOUND;
    private long empIndexLowerBound = EMP_INDEX_LOWER_BOUND;
    private long empIndexUpperBound = EMP_INDEX_UPPER_BOUND;

    /**
     * Constructor.
     *
     * @param departmentRepository the {@link DepartmentRepository}
     * @param employeeRepository   the {@link EmployeeRepository}
     */
    public SampleDatasetLoaderController(DepartmentRepository departmentRepository,
                                         EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * The controller creator used by tests.
     *
     * @param departmentRepository the {@link DepartmentRepository}
     * @param employeeRepository   the {@link EmployeeRepository}
     * @param depIndexLowerBound   the index lower bound for the {@link Department}
     * @param depIndexUpperBound   the index upper bound for the {@link Department}
     * @param empIndexLowerBound   the index lower bound for the {@link Employee}
     * @param empIndexUpperBound   the index upper bound for the {@link Employee}
     * @return the sample dataset loader controller
     */
    public static SampleDatasetLoaderController of(DepartmentRepository departmentRepository,
                                                   EmployeeRepository employeeRepository,
                                                   long depIndexLowerBound, long depIndexUpperBound,
                                                   long empIndexLowerBound, long empIndexUpperBound) {

        final SampleDatasetLoaderController controller = new SampleDatasetLoaderController(departmentRepository,
                employeeRepository);
        controller.depIndexLowerBound = depIndexLowerBound;
        controller.depIndexUpperBound = depIndexUpperBound;
        controller.empIndexLowerBound = empIndexLowerBound;
        controller.empIndexUpperBound = empIndexUpperBound;
        return controller;
    }

    /**
     * Loads the sample dataset for the {@link Department}s with the {@link Employee}s.
     *
     * @param depIndex the index for the {@link Department}
     * @param empIndex the index for the {@link Employee}
     * @return the dataset loading confirmation response
     */
    @GetMapping(LOAD_SAMPLE_DATASET_PATH)
    public String loadSampleDataset(Long depIndex, Long empIndex) {

        depIndexUpperBound = Optional.ofNullable(depIndex).orElse(DEP_INDEX_UPPER_BOUND);
        empIndexUpperBound = Optional.ofNullable(empIndex).orElse(EMP_INDEX_UPPER_BOUND);
        departmentRepository.deleteAll();
        employeeRepository.deleteAll();
        createAndSaveDepartmentList();
        logger.info("loadSampleDataset(): dep bound lower/upper[{}/{}], emp bound lower/upper[{}/{}]",
                depIndexLowerBound, depIndexUpperBound, empIndexLowerBound, empIndexUpperBound);
        return LOAD_SAMPLE_DATASET_RESULT;
    }

    /**
     * Creates the list of {@link Department}s
     */
    private void createAndSaveDepartmentList() {

        List<Department> departmentList = LongStream.rangeClosed(depIndexLowerBound, depIndexUpperBound).boxed()
                .map(Department::fromIndex).toList();
        departmentList = departmentRepository.saveAll(departmentList);
        departmentList.forEach(this::addEmployeesToDepartment);
        departmentRepository.saveAll(departmentList);
    }

    /**
     * Adds the list of the {@link Employee}s to the {@link Department}
     *
     * @param department the {@link Department}
     */
    private void addEmployeesToDepartment(Department department) {

        List<Employee> employeeList = LongStream.rangeClosed(empIndexLowerBound, empIndexUpperBound).boxed()
                .map(idx -> fromIndexes(department.getId(), idx)).toList();
        employeeList.forEach(employee -> employee.setDepartment(department));
        employeeList = employeeRepository.saveAll(employeeList);
        department.getEmployees().addAll(employeeList);
    }

    /**
     * Creates the {@link Employee} from the {@link Department} index and the {@link Employee} index.
     *
     * @param depIndex the index for the {@link Department}
     * @param empIndex the index for the {@link Employee}
     * @return the {@link Employee}
     */
    public static Employee fromIndexes(long depIndex, long empIndex) {

        return new Employee(EMP_INDEX_FUN.applyAsLong(depIndex, empIndex), EMP_F_NAME_FUN.apply(depIndex, empIndex),
                EMP_L_NAME_FUN.apply(depIndex, empIndex));
    }

}
