package kp.company.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kp.Constants;

import java.util.Objects;

/**
 * Simple JavaBean domain object representing an employee.
 * <p>
 * The 'equals()' and 'hashCode()' methods are overridden because instances of the subclasses are in Sets.
 * </p>
 */
public class Employee {

    private long id;

    private String firstName;

    private String lastName;

    @JsonIgnoreProperties("employees")
    private Department department;

    /**
     * Default constructor.
     */
    public Employee() {
    }

    /**
     * Parameterized constructor.
     *
     * @param id         the id
     * @param firstName  the first name
     * @param lastName   the last name
     * @param department the {@link Department}
     */
    public Employee(long id, String firstName, String lastName, Department department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
    }

    /**
     * Creates the employee from the {@link Department} and the employee index.
     *
     * @param department the {@link Department}
     * @param empIndex   the employee index
     * @return the employee
     */
    public static Employee fromIndexes(Department department, long empIndex) {
        return new Employee(Constants.EMP_INDEX_FUN.applyAsLong(department.getId(), empIndex),
                Constants.EMP_F_NAME_FUN.apply(department.getId(), empIndex),
                Constants.EMP_L_NAME_FUN.apply(department.getId(), empIndex), department);
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
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return this.lastName;
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
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * {@inheritDoc}
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
