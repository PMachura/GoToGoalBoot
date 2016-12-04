/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.jsonSerializer.workout;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import gotogoal.model.workout.Exercise;
import gotogoal.rest.resource.workout.ExerciseResource;

/**
 *
 * @author Przemek
 */
public abstract class ExerciseResourceMixIn extends ExerciseResource {

    public ExerciseResourceMixIn(Exercise exercise) {
        super(exercise);
    }
    
    @Override
    @JsonUnwrapped
    public abstract Exercise getExercise();
}
