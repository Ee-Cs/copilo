package kp;

import kp.proto.Department;
import kp.proto.Employee;
import kp.proto.Title;

import java.util.List;

/**
 * Contains constant values used in the application.
 */
public class Constants {
    /**
     * The departments list.
     */
    public static final List<Department> DEPARTMENTS_LIST = List.of(
            Department.newBuilder()
                    .setId(1)
                    .setName("Dep-1")
                    .addAllEmployee(List.of(
                            Employee.newBuilder()
                                    .setId(11)
                                    .setFirstName("Emp-F-N-11")
                                    .setLastName("Emp-L-N-11")
                                    .setTitle(Title.ANALYST)
                                    .build(),
                            Employee.newBuilder()
                                    .setId(12)
                                    .setFirstName("Emp-F-N-12")
                                    .setLastName("Emp-L-N-12")
                                    .setTitle(Title.DEVELOPER)
                                    .build()))
                    .build(),
            Department.newBuilder()
                    .setId(2)
                    .setName("Dep-2")
                    .addAllEmployee(List.of(
                            Employee.newBuilder()
                                    .setId(21)
                                    .setFirstName("Emp-F-N-21")
                                    .setLastName("Emp-L-N-21")
                                    .setTitle(Title.DEVELOPER)
                                    .build(),
                            Employee.newBuilder()
                                    .setId(22)
                                    .setFirstName("Emp-F-N-22")
                                    .setLastName("Emp-L-N-22")
                                    .setTitle(Title.MANAGER)
                                    .build()))
                    .build()
    );

    /**
     * Private constructor to prevent instantiation.
     */
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}