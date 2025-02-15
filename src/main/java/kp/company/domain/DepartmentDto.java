package kp.company.domain;

/**
 * The simple DTO for the {@link Department}
 *
 * @param departmentName the {@link Department} name
 * @param employeesCount the count of the {@link Employee}s
 */
public record DepartmentDto(String departmentName, int employeesCount) {
}