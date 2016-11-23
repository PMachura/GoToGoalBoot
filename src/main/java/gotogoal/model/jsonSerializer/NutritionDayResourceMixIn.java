/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model.jsonSerializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import gotogoal.model.FoodProduct;
import gotogoal.model.NutritionDay;
import gotogoal.rest.resource.NutritionDayResource;

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
