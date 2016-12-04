/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.workout;

import gotogoal.model.workout.Workout;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author Przemek
 */
public class WorkoutResource extends ResourceSupport {
    
    private Workout workout;
    public WorkoutResource(Workout workout){
        this.workout = workout;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }
    
    
}
