/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model.jsonSerializer;

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
import gotogoal.model.FoodProduct;
import gotogoal.model.NutritionDay;
import gotogoal.model.NutritionUnit;
import gotogoal.model.NutritionUnitFoodProduct;
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
class NutritionDayDeserializerForNutritionUnit extends JsonDeserializer<NutritionDay> {

    @Override
    public NutritionDay deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        System.out.println("@@@@ NUTRITION DAY DESERIALIZER");
        JsonNode node = jp.getCodec().readTree(jp);
        Integer id = (Integer) node.get("id").numberValue();
        NutritionDay nutritionDay = new NutritionDay();
        nutritionDay.setId(new Long(id));
        return nutritionDay;
    }
}

class NutritionUnitsFoodProductsDeserializerForNutritionUnit extends JsonDeserializer<Collection<NutritionUnitFoodProduct>> {

    @Override
    public Collection<NutritionUnitFoodProduct> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        System.out.println("@@@ JESTEM W DESERIALIZERZRE");
        Collection<NutritionUnitFoodProduct> nutritionUnitsFoodProducts = new ArrayList<NutritionUnitFoodProduct>();

        JsonNode node = jp.getCodec().readTree(jp);
        if (node.isArray()) {
            System.out.println("@@@@ JESTEM TABLICA");
            
            Iterator<JsonNode> iterator = node.elements();
            
            System.out.println("@@ PRZED PETLA");
            while (iterator.hasNext()) {
                System.out.println("@@ PETLA");
                JsonNode eatenFoodProductsNode = iterator.next();

                NutritionUnitFoodProduct nutritionUnitFoodProduct = new NutritionUnitFoodProduct();
                FoodProduct foodProduct = new FoodProduct();

                Long foodProductId = eatenFoodProductsNode.get("id").numberValue().longValue();
                System.out.println("@@ ID PRODUKTU " + foodProductId);
                foodProduct.setId(foodProductId);
                nutritionUnitFoodProduct.setFoodProduct(foodProduct);

                Float grams = eatenFoodProductsNode.get("grams").numberValue().floatValue();
                System.out.println("@@ PETLA GRAMY " + grams);
                nutritionUnitFoodProduct.setGrams(grams);

                if (eatenFoodProductsNode.has("relationEntityId")) {
                    Long id = eatenFoodProductsNode.get("relationEntityId").numberValue().longValue();
                    nutritionUnitFoodProduct.setId(id);
                }
                nutritionUnitsFoodProducts.add(nutritionUnitFoodProduct);
            }
        }else{
            System.out.println("@@@@ NIE Jestem tablica");
        }

        return nutritionUnitsFoodProducts;

    }
}

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

// @JsonDeserialize(using = TestNutritionDayDeserializer.class)
public abstract class NutritionUnitMixIn extends NutritionUnit {

    @Override
    public abstract Long getId();

    @Override
    public abstract LocalTime getTime();

    @Override
    @JsonSerialize(using = NutritionDaySerializerForNutritionUnit.class)
    public abstract NutritionDay getNutritionDay();

    @Override
    @JsonDeserialize(using = NutritionDayDeserializerForNutritionUnit.class)
    public abstract void setNutritionDay(NutritionDay nutritionDay);

    @Override
    @JsonProperty("eatenFoodProducts")
    @JsonSerialize(using = NutritionUnitFoodProductForNutritionUnitSerializer.class)
    public abstract List<NutritionUnitFoodProduct> getNutritionUnitsFoodProducts();

    @Override
    @JsonProperty("eatenFoodProducts")
    @JsonDeserialize(using = NutritionUnitsFoodProductsDeserializerForNutritionUnit.class)
    public abstract void setNutritionUnitsFoodProducts(List<NutritionUnitFoodProduct> nutritionUnitsFoodProducts);

}
