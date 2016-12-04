/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.assembler.workout;

import gotogoal.model.workout.Workout;
import gotogoal.model.workout.WorkoutDay;
import gotogoal.rest.controller.user.UserRestController;
import gotogoal.rest.controller.workout.WorkoutDayRestController;
import gotogoal.rest.controller.workout.WorkoutRestController;
import gotogoal.rest.resource.workout.WorkoutDayResource;
import gotogoal.rest.resource.workout.WorkoutResource;
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
public class WorkoutResourceAssembler extends ResourceAssemblerSupport<Workout, WorkoutResource> {

    public WorkoutResourceAssembler() {
        super(WorkoutRestController.class, WorkoutResource.class);
    }

    @Override
    public WorkoutResource toResource(Workout workout) {
        WorkoutResource workoutResource = new WorkoutResource(workout);
        workoutResource.add(linkTo(WorkoutRestController.class, workout.getWorkoutDay().getUser().getId(),
                workout.getWorkoutDay().getId()).slash(workout.getId()).withSelfRel());
        workoutResource.add(linkTo(WorkoutDayRestController.class,workout.getWorkoutDay().getUser().getId()).slash(workout.getWorkoutDay().getId()).withRel("workoutDay"));
        workoutResource.add(linkTo(UserRestController.class).slash(workout.getWorkoutDay().getUser().getId()).withRel("user"));
        return workoutResource;
    }
    
    public Collection<WorkoutResource> toResource(Collection<Workout> workouts) {
        return Arrays.asList(workouts.stream()
                .map(this::toResource)
                .toArray(WorkoutResource[]::new));
    }
    
}
