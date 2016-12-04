/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.workout;

import gotogoal.model.workout.WorkoutDay;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author Przemek
 */
public class WorkoutDayResource extends ResourceSupport {
    private WorkoutDay workoutDay;
    
    public WorkoutDayResource(){}
    public WorkoutDayResource(WorkoutDay workoutDay){
        this.workoutDay = workoutDay;
    }

    public WorkoutDay getWorkoutDay() {
        return workoutDay;
    }

    public void setWorkoutDay(WorkoutDay workoutDay) {
        this.workoutDay = workoutDay;
    }
    
    
}
