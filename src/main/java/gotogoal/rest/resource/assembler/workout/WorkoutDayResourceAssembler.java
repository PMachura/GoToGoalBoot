/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.assembler.workout;

import gotogoal.model.nutrition.NutritionDay;
import gotogoal.model.workout.WorkoutDay;
import gotogoal.rest.controller.user.UserRestController;
import gotogoal.rest.controller.workout.WorkoutDayRestController;
import gotogoal.rest.controller.workout.WorkoutRestController;
import gotogoal.rest.resource.workout.WorkoutDayResource;
import org.springframework.data.domain.Page;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 *
 * @author Przemek
 */
@Component
public class WorkoutDayResourceAssembler extends ResourceAssemblerSupport<WorkoutDay, WorkoutDayResource> {

    public WorkoutDayResourceAssembler() {
        super(WorkoutDayRestController.class, WorkoutDayResource.class);
    }

    @Override
    public WorkoutDayResource toResource(WorkoutDay workoutDay) {
        WorkoutDayResource workoutDayResource = new WorkoutDayResource(workoutDay);
        workoutDayResource.add(linkTo(WorkoutDayRestController.class,workoutDay.getUser().getId()).slash(workoutDay.getId()).withSelfRel());
          workoutDayResource.add(linkTo(UserRestController.class).slash(workoutDay.getUser().getId()).withRel("user"));
        workoutDayResource.add(linkTo(WorkoutRestController.class,workoutDay.getUser().getId(),workoutDay.getId()).withRel("workouts"));
        return workoutDayResource;
    }
      
     public Page<WorkoutDayResource> toResource(Page<WorkoutDay> workoutDayPage) {
        return workoutDayPage.map(this::toResource);
    }
    
}
