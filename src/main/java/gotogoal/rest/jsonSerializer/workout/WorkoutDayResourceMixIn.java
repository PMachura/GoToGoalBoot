/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.jsonSerializer.workout;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import gotogoal.model.workout.WorkoutDay;
import gotogoal.rest.resource.workout.WorkoutDayResource;

/**
 *
 * @author Przemek
 */
public abstract class WorkoutDayResourceMixIn extends WorkoutDayResource {
    
    
    @Override
    @JsonUnwrapped
    public abstract WorkoutDay getWorkoutDay();
    
}
