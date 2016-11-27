/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.jsonSerializer.nutrition;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import gotogoal.model.nutrition.FoodProduct;
import gotogoal.rest.resource.nutrition.FoodProductResource;

/**
 *
 * @author Przemek
 */
public abstract class FoodProductResourceMixIn extends FoodProductResource {
      
    @Override
    @JsonUnwrapped
    abstract public FoodProduct getFoodProduct();
}
