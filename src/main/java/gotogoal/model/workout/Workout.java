/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model.workout;

import gotogoal.config.LocalTimeAttributeConverter;
import gotogoal.model.nutrition.MealFoodProduct;
import gotogoal.model.nutrition.NutritionDay;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Przemek
 */
@Entity
public class Workout {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Convert(converter = LocalTimeAttributeConverter.class)
    private LocalTime time;
    
    @NotNull
    @Convert(converter = LocalTimeAttributeConverter.class)
    private LocalTime startTime;

    @ManyToOne
    private WorkoutDay workoutDay;

    @OneToMany(mappedBy = "workout")
    private Collection<WorkoutExercise> workoutExercises;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public WorkoutDay getWorkoutDay() {
        return workoutDay;
    }

    public void setWorkoutDay(WorkoutDay workoutDay) {
        this.workoutDay = workoutDay;
    }

    public Collection<WorkoutExercise> getWorkoutExercises() {
        return workoutExercises;
    }

    public void setWorkoutExercises(Collection<WorkoutExercise> workoutExercises) {
        this.workoutExercises = workoutExercises;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    
    
}
