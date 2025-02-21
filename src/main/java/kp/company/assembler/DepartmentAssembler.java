package kp.company.assembler;

import kp.Constants;
import kp.company.controller.DepartmentController;
import kp.company.domain.Department;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The {@link RepresentationModelAssembler} implementation for the {@link Department}.
 * <ul>
 * <li>The instance of the {@link Department} is wrapped into the {@link EntityModel} instance.</li>
 * <li>The collection of the {@link Department}s is wrapped into the {@link CollectionModel} instance.</li>
 * </ul>
 */
@Component
public class DepartmentAssembler implements RepresentationModelAssembler<Department, EntityModel<Department>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityModel<Department> toModel(@NonNull Department department) {

        return EntityModel.of(department,
                linkTo(methodOn(DepartmentController.class).findDepartmentById(department.getId())).withSelfRel(),
                linkTo(methodOn(DepartmentController.class).findAllDepartments())
                        .withRel(Constants.DEPARTMENTS_LINK_RELATION)
        );
    }

}