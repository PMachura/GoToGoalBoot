/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model.jsonSerializer;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import gotogoal.model.NutritionUnit;
import gotogoal.rest.resource.NutritionUnitResource;

/**
 *
 * @author Przemek
 */
public abstract class NutritionUnitResourceMixIn extends NutritionUnitResource {
   
    @Override
    @JsonUnwrapped
    abstract public NutritionUnit getNutritionUnit();
}
