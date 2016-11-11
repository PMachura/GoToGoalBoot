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
import gotogoal.model.NutritionUnit;
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

class NutritionUnitForDailyNutritionSerializer extends JsonSerializer<List<NutritionUnit>> {

    @Override
    public void serialize(List<NutritionUnit> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartArray();
        for (NutritionUnit nutritionUnit : value) {
            gen.writeStartObject();
            gen.writeNumberField("id", nutritionUnit.getId());
            gen.writeObjectField("time", nutritionUnit.getTime());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}


@JsonPropertyOrder({"id", "date", "user", "nutritionUnits"})
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
    @JsonSerialize(using = NutritionUnitForDailyNutritionSerializer.class)
    public abstract List<NutritionUnit> getNutritionUnits();

}
