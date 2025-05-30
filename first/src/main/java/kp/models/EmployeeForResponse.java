package kp.models;

import kp.proto.Employee;

/**
 * Represents an employee.
 *
 * @param id        the id
 * @param firstName the first name
 * @param lastName  the last name
 * @param title     the title
 */
public record EmployeeForResponse(
        int id, String firstName, String lastName, TitleForResponse title) {
    /**
     * Parameterized constructor.
     *
     * @param employee the employee
     */
    public EmployeeForResponse(Employee employee) {
        this(employee.getId(), employee.getFirstName(), employee.getLastName(),
                TitleForResponse.valueOf(employee.getTitle().toString()));
    }
}
