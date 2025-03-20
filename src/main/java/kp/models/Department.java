package kp.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Represents a department.
 */
public class Department {
    private Long id;
    private String name;
    private BigDecimal budget;
    private OffsetDateTime createdAt;
    private List<Employee> employees;

    /**
     * Default constructor.
     */
    public Department() {
        // constructor is empty
    }

    /**
     * Parameterized constructor.
     *
     * @param id        the department id
     * @param name      the department name
     * @param budget    the budget
     * @param employees the list of employees
     */
    public Department(Long id, String name, BigDecimal budget, List<Employee> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
        this.budget = budget;
        this.createdAt = OffsetDateTime.now();
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
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
     * Gets the budget.
     *
     * @return the budget
     */
    public BigDecimal getBudget() {
        return budget;
    }

    /**
     * Sets the budget.
     *
     * @param budget the budget to set
     */
    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    /**
     * Gets the created at.
     *
     * @return the createdAt
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the created at.
     *
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the employees.
     *
     * @return the employees
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * Sets the employees.
     *
     * @param employees the employees to set
     */
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

}
