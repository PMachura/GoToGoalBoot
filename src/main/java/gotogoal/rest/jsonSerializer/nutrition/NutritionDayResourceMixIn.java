/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.jsonSerializer.nutrition;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import gotogoal.model.nutrition.FoodProduct;
import gotogoal.model.nutrition.NutritionDay;
import gotogoal.rest.resource.nutrition.NutritionDayResource;

/**
 *
 * @author Przemek
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class NutritionDayResourceMixIn extends NutritionDayResource {
    @Override
    @JsonUnwrapped
    abstract public NutritionDay getNutritionDay();
}
