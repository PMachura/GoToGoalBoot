/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model.workout;

import gotogoal.config.LocalTimeAttributeConverter;
import gotogoal.model.nutrition.FoodProduct;
import gotogoal.model.nutrition.Meal;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;

/**
 *
 * @author Przemek
 */
@Entity
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Workout workout;
    
    @ManyToOne
    private Exercise exercise;
    
    private Float series;
    
    private Float repeats;
    
    private Float intensity;
    
    private Float charge;
    
    @Convert(converter = LocalTimeAttributeConverter.class)
    private LocalTime time;

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Float getSeries() {
        return series;
    }

    public void setSeries(Float series) {
        this.series = series;
    }

    public Float getRepeats() {
        return repeats;
    }

    public void setRepeats(Float repeats) {
        this.repeats = repeats;
    }

    public Float getIntensity() {
        return intensity;
    }

    public void setIntensity(Float intensity) {
        this.intensity = intensity;
    }

    public Float getCharge() {
        return charge;
    }

    public void setCharge(Float charge) {
        this.charge = charge;
    }

  
    
    
}
