package kp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kp.company.domain.Department;
import kp.company.domain.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.LongStream;

import static kp.Constants.*;

/**
 * The sample dataset.
 *
 * <p>
 * The standard dataset:
 * <ol>
 * <li>Department
 * <ol>
 * <li>Employee
 * <li>Employee
 * </ol>
 * <li>Department
 * <ol>
 * <li>Employee
 * <li>Employee
 * </ol>
 * </ol>
 */
public class SampleDataset {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static long depIndexLowerBound = DEP_INDEX_LOWER_BOUND;
    private static long depIndexUpperBound = DEP_INDEX_UPPER_BOUND;
    private static long empIndexLowerBound = EMP_INDEX_LOWER_BOUND;
    private static long empIndexUpperBound = EMP_INDEX_UPPER_BOUND;

    private static final List<Department> departments = new ArrayList<>();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Private constructor to prevent instantiation.
     */
    private SampleDataset() {
    }

    /**
     * Loads the sample dataset.
     *
     * @param depIndex the {@link Department} index upper bound
     * @param empIndex the {@link Employee} index upper bound
     */
    public static void loadDataset(long depIndex, long empIndex) {

        depIndexUpperBound = depIndex;
        empIndexUpperBound = empIndex;
        loadDataset();
    }

    /**
     * Loads the sample dataset for tests.
     *
     * @param depIndexLowerBoundParam the {@link Department} index lower bound
     * @param depIndexUpperBoundParam the {@link Department} index upper bound
     * @param empIndexLowerBoundParam the {@link Employee} index lower bound
     * @param empIndexUpperBoundParam the {@link Employee} index upper bound
     */
    public static void loadDataset(long depIndexLowerBoundParam, long depIndexUpperBoundParam,
                                   long empIndexLowerBoundParam, long empIndexUpperBoundParam) {

        depIndexLowerBound = depIndexLowerBoundParam;
        depIndexUpperBound = depIndexUpperBoundParam;
        empIndexLowerBound = empIndexLowerBoundParam;
        empIndexUpperBound = empIndexUpperBoundParam;
        loadDataset();
    }

    /**
     * Deletes the sample dataset.
     */
    public static void deleteDataset() {

        departments.clear();
        writeDatasetToTemporaryFile();
    }

    /**
     * Finds the {@link Department} by id.
     *
     * @param id the id
     * @return the {@link Optional} with the {@link Department}
     */
    public static Optional<Department> findDepartmentById(long id) {

        readDatasetFromTemporaryFile();
        return departments.stream().filter(dep -> id == dep.getId()).findFirst();
    }

    /**
     * Finds the {@link Department}s.
     *
     * @return the {@link Optional} with the list of {@link Department}s
     */
    public static Optional<List<Department>> findAllDepartments() {

        readDatasetFromTemporaryFile();
        return Optional.of(departments).filter(list -> !list.isEmpty()).or(Optional::empty);
    }

    /**
     * Saves the {@link Department}.
     *
     * @param department the {@link Department}
     */
    public static void saveDepartment(Department department) {

        readDatasetFromTemporaryFile();
        boolean addNewFlag = true;
        for (int i = 0; i < departments.size(); i++) {
            if (department.getId() == departments.get(i).getId()) {
                departments.set(i, department);
                addNewFlag = false;
                break;
            }
        }
        if (addNewFlag) {
            departments.add(department);
        }
        writeDatasetToTemporaryFile();
    }

    /**
     * Deletes the {@link Department} by id.
     *
     * @param id the id
     */
    public static void deleteDepartmentById(long id) {

        readDatasetFromTemporaryFile();
        for (Department dep : departments) {
            if (id == dep.getId()) {
                departments.remove(dep);
                break;
            }
        }
        writeDatasetToTemporaryFile();
    }

    /**
     * Finds the {@link Employee} by id.
     *
     * @param id the id
     * @return the {@link Optional} with the {@link Employee}
     */
    public static Optional<Employee> findEmployeeById(long id) {

        readDatasetFromTemporaryFile();
        return departments.stream().flatMap(dep -> dep.getEmployees().stream())
                .filter(emp -> id == emp.getId()).findFirst();
    }

    /**
     * Finds the {@link Employee}s.
     *
     * @return the {@link Optional} with the list of {@link Employee}s
     */
    public static Optional<List<Employee>> findAllEmployees() {

        readDatasetFromTemporaryFile();
        final List<Employee> employeeList = departments.stream()
                .flatMap(dep -> dep.getEmployees().stream()).toList();
        return Optional.of(employeeList).filter(list -> !list.isEmpty()).or(Optional::empty);
    }

    /**
     * Saves the {@link Employee}.
     *
     * @param employee the {@link Employee}
     */
    public static void saveEmployee(Employee employee) {

        readDatasetFromTemporaryFile();
        for (Department dep : departments) {
            if (employee.getDepartment().getId() == dep.getId()) {
                boolean addNewFlag = true;
                for (int i = 0; i < dep.getEmployees().size(); i++) {
                    if (employee.getId() == dep.getEmployees().get(i).getId()) {
                        dep.getEmployees().set(i, employee);
                        addNewFlag = false;
                    }
                }
                if (addNewFlag) {
                    dep.getEmployees().add(employee);
                }
            }
        }
        writeDatasetToTemporaryFile();
    }

    /**
     * Deletes the {@link Employee} by id.
     *
     * @param id the employee id
     */
    public static void deleteEmployeeById(long id) {

        readDatasetFromTemporaryFile();
        removeEmployeeFromDepartment(id);
        writeDatasetToTemporaryFile();
    }

    /**
     * Removes the {@link Employee} from the {@link Department}.
     *
     * @param id the employee id
     */
    private static void removeEmployeeFromDepartment(long id) {

        for (Department dep : departments) {
            final List<Employee> employeeList = dep.getEmployees();
            for (Employee emp : employeeList) {
                if (id == emp.getId()) {
                    employeeList.remove(emp);
                    return;
                }
            }
        }
    }

    /**
     * Loads the sample dataset for the {@link Department}s with the {@link Employee}s.
     */
    private static void loadDataset() {

        departments.clear();
        LongStream.rangeClosed(depIndexLowerBound, depIndexUpperBound).boxed().map(Department::fromIndex)
                .map(SampleDataset::addEmployeesToDepartment).forEach(departments::add);
        writeDatasetToTemporaryFile();
        logger.info("loadDataset(): dep bound lower/upper[{}/{}], emp bound lower/upper[{}/{}]",
                depIndexLowerBound, depIndexUpperBound, empIndexLowerBound, empIndexUpperBound);
    }

    /**
     * Writes dataset to temporary file.
     */
    public static void writeDatasetToTemporaryFile() {
        try {
            objectMapper.writeValue(SAMPLE_DATASET_FILE, departments);
        } catch (IOException e) {
            logger.error("writeDatasetToTemporaryFile(): IOException[{}], file[{}]", e.getMessage(), SAMPLE_DATASET_FILE);
        }
        logger.debug("writeDatasetToTemporaryFile(): file[{}]", SAMPLE_DATASET_FILE);
    }

    /**
     * Reads dataset from temporary file.
     */
    public static void readDatasetFromTemporaryFile() {

        departments.clear();
        if (!SAMPLE_DATASET_FILE.exists()) {
            writeDatasetToTemporaryFile();
            logger.info("readDatasetFromTemporaryFile(): recreated file[{}]", SAMPLE_DATASET_FILE);
        }
        try {
            final List<Department> departmentsRead = objectMapper.readValue(SAMPLE_DATASET_FILE, new TypeReference<>() {
            });
            departmentsRead.forEach(dep -> {
                final List<Employee> employeesList = new ArrayList<>();
                final Department department = new Department(dep.getId(), dep.getName(), employeesList);
                departments.add(department);
                if (Objects.nonNull(dep.getEmployees())) {
                    dep.getEmployees().stream()
                            .map(emp -> new Employee(emp.getId(), emp.getFirstName(), emp.getLastName(),
                                    new Department(dep.getId(), dep.getName(), new ArrayList<>())))
                            .forEach(employeesList::add);
                }
            });
        } catch (IOException e) {
            logger.error("readDatasetFromTemporaryFile(): IOException[{}], file[{}]", e.getMessage(), SAMPLE_DATASET_FILE);
        }
        logger.debug("readDatasetFromTemporaryFile(): file[{}]", SAMPLE_DATASET_FILE);
    }

    /**
     * Adds list of the {@link Employee}s to the {@link Department}.
     *
     * @param department the {@link Department}
     * @return the {@link Department}
     */
    private static Department addEmployeesToDepartment(Department department) {

        final List<Employee> employeeList = LongStream.rangeClosed(empIndexLowerBound, empIndexUpperBound).boxed()
                .map(idx -> Employee.fromIndexes(department, idx)).toList();
        department.getEmployees().addAll(employeeList);
        return department;
    }

}
