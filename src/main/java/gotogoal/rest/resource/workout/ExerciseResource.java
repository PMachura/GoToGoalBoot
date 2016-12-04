/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.workout;

import gotogoal.model.workout.Exercise;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author Przemek
 */
public class ExerciseResource extends ResourceSupport {
 
    private Exercise exercise;
    public ExerciseResource(Exercise exercise){
        this.exercise = exercise;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
    
    
}
