/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.jsonSerializer.workout;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gotogoal.model.nutrition.Meal;
import gotogoal.model.nutrition.MealFoodProduct;
import gotogoal.model.user.User;
import gotogoal.model.workout.Exercise;
import gotogoal.model.workout.Workout;
import gotogoal.model.workout.WorkoutDay;
import gotogoal.model.workout.WorkoutExercise;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Przemek
 */
class WorkoutForWorkoutDaySerializer extends JsonSerializer<Collection<Workout>> {

    public void serializeWorkoutExercises(Collection<WorkoutExercise> workoutExercises, JsonGenerator gen) throws IOException {
        gen.writeArrayFieldStart("doneExercises");
        for (WorkoutExercise workoutExercise : workoutExercises) {
            gen.writeStartObject();
            gen.writeNumberField("relationEntityId", workoutExercise.getId());
            gen.writeNumberField("id", workoutExercise.getExercise().getId());
            gen.writeStringField("name", workoutExercise.getExercise().getName());
            gen.writeNumberField("series", workoutExercise.getSeries());
            gen.writeNumberField("intensity", workoutExercise.getIntensity());
            gen.writeNumberField("charge", workoutExercise.getCharge());
            gen.writeNumberField("repeats", workoutExercise.getRepeats());
            gen.writeObjectField("time", workoutExercise.getTime());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }

    @Override
    public void serialize(Collection<Workout> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartArray();
        for (Workout workout : value) {
            gen.writeStartObject();
            gen.writeNumberField("id", workout.getId());
            gen.writeObjectField("time", workout.getTime());
            gen.writeObjectField("startTime", workout.getStartTime());
            if (workout.getWorkoutExercises() != null) {
                serializeWorkoutExercises(workout.getWorkoutExercises(), gen);
            }
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}

class UserForWorkoutDaySerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeNumber(value.getId());
    }
}

class WorkoutDayDeserializer extends JsonDeserializer<WorkoutDay> {

    public Collection<WorkoutExercise> workoutExercisesDeserialize(JsonNode workoutNode) {
        if (workoutNode.has("doneExercises")) {
            JsonNode doneExercisesArrayNode = workoutNode.get("doneExercises");
            Collection<WorkoutExercise> workoutExercises = new ArrayList<WorkoutExercise>();
            if (doneExercisesArrayNode.isArray()) {
                Iterator<JsonNode> workoutExercisesNodes = doneExercisesArrayNode.elements();
                while (workoutExercisesNodes.hasNext()) {
                    JsonNode workoutExercisesNode = workoutExercisesNodes.next();
                    WorkoutExercise workoutExercise = new WorkoutExercise();
                    Exercise exercise = new Exercise();

                    Float series = workoutExercisesNode.has("series") ? workoutExercisesNode.get("series").numberValue().floatValue() : null;
                    workoutExercise.setSeries(series);

                    Float repeats = workoutExercisesNode.has("repeats") ? workoutExercisesNode.get("repeats").numberValue().floatValue() : null;
                    workoutExercise.setRepeats(repeats);

                    Float intensity = workoutExercisesNode.has("intensity") ? workoutExercisesNode.get("intensity").numberValue().floatValue() : null;
                    workoutExercise.setIntensity(intensity);

                    Float charge = workoutExercisesNode.has("charge") ? workoutExercisesNode.get("charge").numberValue().floatValue() : null;
                    workoutExercise.setCharge(charge);

                    LocalTime time = workoutExercisesNode.has("time") && !workoutExercisesNode.get("time").textValue().isEmpty() ? LocalTime.parse(workoutExercisesNode.get("time").textValue()) : null;
                    workoutExercise.setTime(time);

                    Long exerciseId = workoutExercisesNode.has("id") ? workoutExercisesNode.get("id").numberValue().longValue() : null;
                    exercise.setId(exerciseId);
                    workoutExercise.setExercise(exercise);

                    Long id = workoutExercisesNode.has("relationEntityId") ? workoutExercisesNode.get("relationEntityId").numberValue().longValue() : null;
                    workoutExercise.setId(id);

                    workoutExercises.add(workoutExercise);
                }
            }
            return workoutExercises;
        }

        return null;

    }

    public Collection<Workout> workoutsDeserializer(JsonNode workoutDayNode) {

        if (workoutDayNode.has("workouts")) {

            JsonNode workoutsArrayNode = workoutDayNode.get("workouts");
            Collection<Workout> workouts = new ArrayList<Workout>();
            if (workoutsArrayNode.isArray()) {

                Iterator<JsonNode> workoutNodes = workoutsArrayNode.elements();
                while (workoutNodes.hasNext()) {
                    JsonNode workoutNode = workoutNodes.next();

                    Workout workout = new Workout();

                    Long workoutId = workoutNode.has("id") ? workoutNode.get("id").numberValue().longValue() : null;
                    workout.setId(workoutId);

                    LocalTime workoutTime = workoutNode.has("time") && !workoutNode.get("time").textValue().isEmpty() ? LocalTime.parse(workoutNode.get("time").textValue()) : null;
                    workout.setTime(workoutTime);

                    LocalTime workoutStartTime = workoutNode.has("startTime") ? LocalTime.parse(workoutNode.get("startTime").textValue()) : null;
                    workout.setStartTime(workoutStartTime);

                    Collection<WorkoutExercise> workoutExercises = workoutExercisesDeserialize(workoutNode);
                    workoutExercises.forEach((WorkoutExercise workoutExercise) -> workoutExercise.setWorkout(workout));
                    workout.setWorkoutExercises(workoutExercises);

                    workouts.add(workout);

                }
            }
            return workouts;
        }
        return null;
    }

    @Override
    public WorkoutDay deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        WorkoutDay workoutDay = new WorkoutDay();

        JsonNode workoutDayNode = jp.getCodec().readTree(jp);

        Long workoutDayId = workoutDayNode.has("id") ? workoutDayNode.get("id").numberValue().longValue() : null;
        LocalDate workoutDayDate = workoutDayNode.has("date") ? LocalDate.parse(workoutDayNode.get("date").textValue()) : null;
        String workoutDayNote = workoutDayNode.has("note") ? workoutDayNode.get("note").textValue() : null;
        workoutDay.setId(workoutDayId);
        workoutDay.setDate(workoutDayDate);
        workoutDay.setNote(workoutDayNote);

        User user = new User();
        Long userId = workoutDayNode.has("user") ? workoutDayNode.get("user").numberValue().longValue() : null;
        user.setId(userId);
        workoutDay.setUser(user);

        Collection<Workout> workouts = workoutsDeserializer(workoutDayNode);
        workouts.forEach((Workout workout) -> workout.setWorkoutDay(workoutDay));
        workoutDay.setWorkouts(workouts);

        return workoutDay;

    }

}

@JsonDeserialize(using = WorkoutDayDeserializer.class)
public abstract class WorkoutDayMixIn extends WorkoutDay {

    @Override

    @JsonProperty("id")
    public abstract Long getId();

    @Override
    @JsonProperty("date")
    public abstract LocalDate getDate();

    @Override
    @JsonSerialize(using = UserForWorkoutDaySerializer.class)
    @JsonProperty("user")
    public abstract User getUser();

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = WorkoutForWorkoutDaySerializer.class)
    public abstract Collection<Workout> getWorkouts();

}
