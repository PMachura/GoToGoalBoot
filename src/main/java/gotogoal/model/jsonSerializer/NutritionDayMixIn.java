/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model.jsonSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gotogoal.model.NutritionDay;
import gotogoal.model.Meal;
import gotogoal.model.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Przemek
 */
class UserAsEmailSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeString(value.getEmail());
    }
}

class MealForDailyNutritionSerializer extends JsonSerializer<List<Meal>> {

    @Override
    public void serialize(List<Meal> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartArray();
        for (Meal meal : value) {
            gen.writeStartObject();
            gen.writeNumberField("id", meal.getId());
            gen.writeObjectField("time", meal.getTime());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}


@JsonPropertyOrder({"id", "date", "user", "meals"})
public abstract class NutritionDayMixIn extends NutritionDay {

    @Override

    @JsonProperty("id")
    public abstract Long getId();

    @Override
    @JsonProperty("date")
    public abstract LocalDate getDate();

    @Override
    @JsonSerialize(using = UserAsEmailSerializer.class)
    @JsonProperty("user")
    public abstract User getUser();

    @Override
    @JsonInclude(Include.NON_NULL)
    @JsonSerialize(using = MealForDailyNutritionSerializer.class)
    public abstract List<Meal> getMeals();

}
