/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.jsonSerializer.nutrition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
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
import gotogoal.model.nutrition.FoodProduct;
import gotogoal.model.nutrition.NutritionDay;
import gotogoal.model.nutrition.Meal;
import gotogoal.model.nutrition.MealFoodProduct;
import gotogoal.model.user.User;
import java.io.IOException;
import java.math.BigDecimal;
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
class NutritionDayDeserializer extends JsonDeserializer<NutritionDay> {

    public Collection<MealFoodProduct> mealFoodProductsDeserialize(JsonNode mealNode) {
        if (mealNode.has("eatenFoodProducts")) {
            JsonNode eatenFoodProductsArrayNode = mealNode.get("eatenFoodProducts");
            Collection<MealFoodProduct> mealsFoodProducts = new ArrayList<MealFoodProduct>();
            if (eatenFoodProductsArrayNode.isArray()) {
                Iterator<JsonNode> eatenFoodProductNodes = eatenFoodProductsArrayNode.elements();
                while (eatenFoodProductNodes.hasNext()) {
                    JsonNode eatenFoodProductNode = eatenFoodProductNodes.next();
                    MealFoodProduct mealFoodProduct = new MealFoodProduct();
                    FoodProduct foodProduct = new FoodProduct();
                    Float grams = eatenFoodProductNode.get("grams").numberValue().floatValue();
                    mealFoodProduct.setGrams(grams);

                    Long foodProductId = eatenFoodProductNode.get("id").numberValue().longValue();
                    foodProduct.setId(foodProductId);
                    mealFoodProduct.setFoodProduct(foodProduct);

                    Long id = eatenFoodProductNode.has("relationEntityId") ? eatenFoodProductNode.get("relationEntityId").numberValue().longValue() : null;
                    mealFoodProduct.setId(id);

                    mealsFoodProducts.add(mealFoodProduct);
                }
            }
            return mealsFoodProducts;
        }

        return null;

    }

    public Collection<Meal> mealsDeserialize(JsonNode nutritionDayNode) {

    
        if (nutritionDayNode.has("meals")) {
   
            JsonNode mealsArrayNode = nutritionDayNode.get("meals");
            Collection<Meal> meals = new ArrayList<Meal>();
            if (mealsArrayNode.isArray()) {

                Iterator<JsonNode> mealNodes = mealsArrayNode.elements();
                while (mealNodes.hasNext()) {
                    JsonNode mealNode = mealNodes.next();

                    Meal meal = new Meal();

                    Long mealId = mealNode.has("id") ? mealNode.get("id").numberValue().longValue() : null;
                    meal.setId(mealId);

                    LocalTime mealTime = LocalTime.parse(mealNode.get("time").textValue());
                    meal.setTime(mealTime);

                    Collection<MealFoodProduct> mealsFoodProducts = mealFoodProductsDeserialize(mealNode);
                    mealsFoodProducts.forEach((MealFoodProduct mealFoodProduct) -> mealFoodProduct.setMeal(meal));
                    meal.setMealsFoodProducts((List<MealFoodProduct>) mealsFoodProducts);

                    meals.add(meal);

                }
            }
            return meals;
        }
        return null;
    }

    @Override
    public NutritionDay deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        NutritionDay nutritionDay = new NutritionDay();

        JsonNode nutritionDayNode = jp.getCodec().readTree(jp);
     
        Long nutritionDayId = nutritionDayNode.has("id") ? nutritionDayNode.get("id").numberValue().longValue() : null;
        LocalDate nutritionDayDate = nutritionDayNode.has("date") ? LocalDate.parse(nutritionDayNode.get("date").textValue()) : null;
        String nutritionDayNoe = nutritionDayNode.has("note") ? nutritionDayNode.get("note").textValue() : null;
        nutritionDay.setId(nutritionDayId);
        nutritionDay.setDate(nutritionDayDate);
        nutritionDay.setNote(nutritionDayNoe);
        

        User user = new User();
        Long userId = nutritionDayNode.has("user") ? nutritionDayNode.get("user").numberValue().longValue() : null;
        user.setId(userId);
        nutritionDay.setUser(user);

        Collection<Meal> meals = mealsDeserialize(nutritionDayNode);
        meals.forEach((Meal meal) -> meal.setNutritionDay(nutritionDay));
        nutritionDay.setMeals((List<Meal>) meals);

        return nutritionDay;

    }

}

class UserForNutritionDaySerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeNumber(value.getId());
    }
}

class MealForDailyNutritionSerializer extends JsonSerializer<List<Meal>> {

    public void serializeMealFoodProducts(Collection<MealFoodProduct> mealFoodProducts, JsonGenerator gen) throws IOException{
        gen.writeArrayFieldStart("eatenFoodProducts");
        for (MealFoodProduct nufp : mealFoodProducts) {
            System.out.println("STEP 3");
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
    
    @Override
    public void serialize(List<Meal> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartArray();
        for (Meal meal : value) {
            gen.writeStartObject();
            gen.writeNumberField("id", meal.getId());
            gen.writeObjectField("time", meal.getTime());
            if(meal.getMealsFoodProducts() != null) serializeMealFoodProducts(meal.getMealsFoodProducts(), gen);    
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}

@JsonPropertyOrder({"id", "date", "user", "meals"})
@JsonDeserialize(using = NutritionDayDeserializer.class)
public abstract class NutritionDayMixIn extends NutritionDay {

    @Override

    @JsonProperty("id")
    public abstract Long getId();

    @Override
    @JsonProperty("date")
    public abstract LocalDate getDate();

    @Override
    @JsonSerialize(using = UserForNutritionDaySerializer.class)
    @JsonProperty("user")
    public abstract User getUser();

    @Override
    @JsonInclude(Include.NON_NULL)
    @JsonSerialize(using = MealForDailyNutritionSerializer.class)
    public abstract List<Meal> getMeals();

}
