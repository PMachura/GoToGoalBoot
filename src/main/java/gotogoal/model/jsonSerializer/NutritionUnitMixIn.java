/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model.jsonSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gotogoal.model.NutritionDay;
import gotogoal.model.NutritionUnit;
import gotogoal.model.NutritionUnitFoodProduct;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Przemek
 */
class NutritionUnitFoodProductForNutritionUnitSerializer extends JsonSerializer<List<NutritionUnitFoodProduct>> {

    @Override
    public void serialize(List<NutritionUnitFoodProduct> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartArray();
        for (NutritionUnitFoodProduct nufp : value) {
            gen.writeStartObject();
            gen.writeNumberField("relationEntityId", nufp.getId());
            gen.writeNumberField("id", nufp.getFoodProduct().getId());
            gen.writeStringField("name", nufp.getFoodProduct().getName());
            gen.writeObjectField("category", nufp.getFoodProduct().getCategory());
            gen.writeNumberField("grams", nufp.getGrams());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }

}

class NutritionDaySerializerForNutritionUnit extends JsonSerializer<NutritionDay> {

    @Override
    public void serialize(NutritionDay value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        //  gen.writeNumber(value.getId());
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("user", value.getUser().getEmail());
        gen.writeObjectField("date", value.getDate());
        gen.writeEndObject();
    }

}

public abstract class NutritionUnitMixIn extends NutritionUnit {

    @Override
    public abstract Long getId();

    @Override
    public abstract LocalDateTime getLocalDateTime();

    @Override
    @JsonSerialize(using = NutritionDaySerializerForNutritionUnit.class)
    public abstract NutritionDay getNutritionDay();

    @Override
    @JsonProperty("foodProducts")
    @JsonSerialize(using = NutritionUnitFoodProductForNutritionUnitSerializer.class)
    public abstract List<NutritionUnitFoodProduct> getNutritionUnitsFoodProducts();

}
