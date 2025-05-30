package kp.models;

import kp.proto.Department;

import java.util.List;

/**
 * Represents a department.
 *
 * @param id        the id
 * @param name      the name
 * @param employees the list of employees
 */
public record DepartmentForResponse(
        int id, String name, List<EmployeeForResponse> employees) {
    /**
     * Parameterized constructor.
     *
     * @param department the department
     */
    public DepartmentForResponse(Department department) {
        this(department.getId(), department.getName(),
                department.getEmployeeList().stream().map(EmployeeForResponse::new).toList());
    }
}
