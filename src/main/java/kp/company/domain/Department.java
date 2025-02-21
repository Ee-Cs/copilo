package kp.company.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kp.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple JavaBean domain object representing a department.
 */
public class Department {

    private long id;

    private String name;

    @JsonIgnoreProperties("department")
    private List<Employee> employees;

    /**
     * Default constructor.
     */
    public Department() {
    }

    /**
     * Parameterized constructor.
     *
     * @param id        the id
     * @param name      the name
     * @param employees the list of the {@link Employee}s
     */
    public Department(long id, String name, List<Employee> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    /**
     * Creates the department from the index.
     *
     * @param depIndex the department index
     * @return the department
     */
    public static Department fromIndex(long depIndex) {
        return new Department(depIndex, Constants.DEP_NAME_FUN.apply(depIndex), new ArrayList<>());
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
