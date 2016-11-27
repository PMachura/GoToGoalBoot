/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.jsonSerializer.nutrition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.LongNode;
import gotogoal.model.nutrition.FoodProduct;
import gotogoal.model.nutrition.NutritionDay;
import gotogoal.model.nutrition.Meal;
import gotogoal.model.nutrition.MealFoodProduct;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Przemek
 */
class NutritionDayDeserializerForMeal extends JsonDeserializer<NutritionDay> {

    @Override
    public NutritionDay deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        Integer id = (Integer) node.get("id").numberValue();
        NutritionDay nutritionDay = new NutritionDay();
        nutritionDay.setId(new Long(id));
        return nutritionDay;
    }
}

class MealsFoodProductsDeserializerForMeal extends JsonDeserializer<Collection<MealFoodProduct>> {

    @Override
    public Collection<MealFoodProduct> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        
        Collection<MealFoodProduct> mealsFoodProducts = new ArrayList<MealFoodProduct>();

        JsonNode node = jp.getCodec().readTree(jp);
        if (node.isArray()) {    
            Iterator<JsonNode> iterator = node.elements();
            
            while (iterator.hasNext()) {
                JsonNode eatenFoodProductsNode = iterator.next();

                MealFoodProduct mealFoodProduct = new MealFoodProduct();
                FoodProduct foodProduct = new FoodProduct();

                Long foodProductId = eatenFoodProductsNode.get("id").numberValue().longValue();
                foodProduct.setId(foodProductId);
                mealFoodProduct.setFoodProduct(foodProduct);

                Float grams = eatenFoodProductsNode.get("grams").numberValue().floatValue();
                mealFoodProduct.setGrams(grams);

                if (eatenFoodProductsNode.has("relationEntityId")) {
                    Long id = eatenFoodProductsNode.get("relationEntityId").numberValue().longValue();
                    mealFoodProduct.setId(id);
                }
                mealsFoodProducts.add(mealFoodProduct);
            }
        }else{
        }

        return mealsFoodProducts;

    }
}

class MealFoodProductForMealSerializer extends JsonSerializer<List<MealFoodProduct>> {

    @Override
    public void serialize(List<MealFoodProduct> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartArray();
        for (MealFoodProduct nufp : value) {
            gen.writeStartObject();
            gen.writeNumberField("relationEntityId", nufp.getId());
            gen.writeNumberField("id", nufp.getFoodProduct().getId());
            gen.writeStringField("name", nufp.getFoodProduct().getName());
            gen.writeObjectField("category", nufp.getFoodProduct().getCategory());
            gen.writeNumberField("grams", nufp.getGrams());
            gen.writeNumberField("calories", nufp.getFoodProduct().getCalories());
            gen.writeNumberField("proteins",nufp.getFoodProduct().getProteins() );
            gen.writeNumberField("carbohydrates",nufp.getFoodProduct().getCarbohydrates() );
            gen.writeNumberField("fats",nufp.getFoodProduct().getFats() );
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }

}

class NutritionDaySerializerForMeal extends JsonSerializer<NutritionDay> {

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

// @JsonDeserialize(using = TestNutritionDayDeserializer.class)
public abstract class MealMixIn extends Meal {

    @Override
    public abstract Long getId();

    @Override
    public abstract LocalTime getTime();

    @Override
    @JsonSerialize(using = NutritionDaySerializerForMeal.class)
    public abstract NutritionDay getNutritionDay();

    @Override
    @JsonDeserialize(using = NutritionDayDeserializerForMeal.class)
    public abstract void setNutritionDay(NutritionDay nutritionDay);

    @Override
    @JsonProperty("eatenFoodProducts")
    @JsonSerialize(using = MealFoodProductForMealSerializer.class)
    public abstract List<MealFoodProduct> getMealsFoodProducts();

    @Override
    @JsonProperty("eatenFoodProducts")
    @JsonDeserialize(using = MealsFoodProductsDeserializerForMeal.class)
    public abstract void setMealsFoodProducts(List<MealFoodProduct> mealsFoodProducts);

}
