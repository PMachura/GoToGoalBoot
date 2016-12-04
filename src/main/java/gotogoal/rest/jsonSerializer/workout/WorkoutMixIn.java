/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.jsonSerializer.workout;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gotogoal.model.nutrition.MealFoodProduct;
import gotogoal.model.workout.Workout;
import gotogoal.model.workout.WorkoutDay;
import gotogoal.model.workout.WorkoutExercise;
import java.io.IOException;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Przemek
 */



class WorkoutDaySerializerForWorkout extends JsonSerializer<WorkoutDay> {

    @Override
    public void serialize(WorkoutDay value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeNumberField("user", value.getUser().getId());
        gen.writeObjectField("date", value.getDate());
        gen.writeEndObject();
    }

}

class WorkoutExerciseForWorkoutSerializer extends JsonSerializer<Collection<WorkoutExercise>>{
    
    @Override
    public void serialize(Collection<WorkoutExercise> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartArray();
        for (WorkoutExercise workoutExercise : value) {
            gen.writeStartObject();
            gen.writeNumberField("relationEntityId", workoutExercise.getId());
            gen.writeNumberField("id", workoutExercise.getExercise().getId());
            gen.writeStringField("name", workoutExercise.getExercise().getName());
            gen.writeNumberField("series", workoutExercise.getSeries());
            gen.writeNumberField("intensity", workoutExercise.getIntensity());
            gen.writeNumberField("charge",workoutExercise.getCharge() );
            gen.writeNumberField("repeats",workoutExercise.getRepeats());
            gen.writeObjectField("time",workoutExercise.getTime());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}


public abstract class WorkoutMixIn extends Workout {
    
    @Override
    public abstract Long getId();

    @Override
    public abstract void setId(Long id);

    @Override
    public abstract LocalTime getTime();

    @Override
    public abstract void setTime(LocalTime time);

    @Override
    @JsonSerialize(using = WorkoutDaySerializerForWorkout.class)
    public abstract WorkoutDay getWorkoutDay();

    @Override
    public abstract void setWorkoutDay(WorkoutDay workoutDay);

    @Override
    @JsonProperty("doneExercises")
    @JsonSerialize(using = WorkoutExerciseForWorkoutSerializer.class)
    public abstract Collection<WorkoutExercise> getWorkoutExercises();

    @Override
    public abstract void setWorkoutExercises(Collection<WorkoutExercise> workoutExercises) ;

    @Override
    public abstract void setStartTime(LocalTime startTime);

    @Override
    public abstract LocalTime getStartTime();
    
    
}
