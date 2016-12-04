/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.jsonSerializer.workout;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import gotogoal.model.workout.Workout;
import gotogoal.rest.resource.workout.WorkoutResource;

/**
 *
 * @author Przemek
 */
public abstract class WorkoutResourceMixIn extends WorkoutResource {

    public WorkoutResourceMixIn(Workout workout) {
        super(workout);
    }
    
    @Override
    @JsonUnwrapped
    public abstract Workout getWorkout();
  
}
