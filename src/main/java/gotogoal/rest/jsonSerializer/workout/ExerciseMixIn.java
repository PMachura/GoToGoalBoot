/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.jsonSerializer.workout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gotogoal.model.workout.Exercise;
import gotogoal.model.workout.WorkoutExercise;
import java.util.Collection;

/**
 *
 * @author Przemek
 */
public abstract class ExerciseMixIn extends Exercise {

    @Override
    public abstract void setWorkoutExercises(Collection<WorkoutExercise> workoutExercises);

    @Override
    @JsonIgnore
    public abstract Collection<WorkoutExercise> getWorkoutExercises();

    @Override
    public abstract void setName(String name);

    @Override
    public abstract String getName();

    @Override
    public abstract void setId(Long id);

    @Override
    public abstract Long getId();
    
    
    
}
