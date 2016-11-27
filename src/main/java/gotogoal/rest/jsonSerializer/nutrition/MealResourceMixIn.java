/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.jsonSerializer.nutrition;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import gotogoal.model.nutrition.Meal;
import gotogoal.rest.resource.nutrition.MealResource;

/**
 *
 * @author Przemek
 */
public abstract class MealResourceMixIn extends MealResource {
   
    @Override
    @JsonUnwrapped
    abstract public Meal getMeal();
}
