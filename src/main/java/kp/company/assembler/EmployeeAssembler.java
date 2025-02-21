package kp.company.assembler;

import kp.Constants;
import kp.company.controller.EmployeeController;
import kp.company.domain.Employee;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The {@link RepresentationModelAssembler} implementation for the {@link Employee}.
 * <ul>
 * <li>The instance of the {@link Employee} is wrapped into the {@link EntityModel} instance.</li>
 * <li>The collection of the {@link Employee}s is wrapped into the {@link CollectionModel} instance.</li>
 * </ul>
 */
@Component
public class EmployeeAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityModel<Employee> toModel(@NonNull Employee employee) {

        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).findEmployeeById(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).findAllEmployees())
                        .withRel(Constants.EMPLOYEES_LINK_RELATION)
        );
    }

}