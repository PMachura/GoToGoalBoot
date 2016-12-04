/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.assembler.workout;

import gotogoal.model.workout.Exercise;
import gotogoal.rest.controller.workout.ExerciseRestController;
import gotogoal.rest.resource.workout.ExerciseResource;
import java.util.Arrays;
import java.util.Collection;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 *
 * @author Przemek
 */
@Component
public class ExerciseResourceAssembler extends ResourceAssemblerSupport<Exercise, ExerciseResource> {

    public ExerciseResourceAssembler() {
        super(ExerciseRestController.class, ExerciseResource.class);
    }

    @Override
    public ExerciseResource toResource(Exercise entity) {
        ExerciseResource exerciseResource = new ExerciseResource(entity);
        exerciseResource.add(linkTo(ExerciseRestController.class).slash(entity.getId()).withSelfRel());
        return exerciseResource;
    }

    public Collection<ExerciseResource> toResource(Collection<Exercise> exercises) {
        return Arrays.asList(exercises.stream()
                .map(this::toResource)
                .toArray(ExerciseResource[]::new));
    }

}
