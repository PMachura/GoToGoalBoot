/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource;

import gotogoal.model.NutritionUnit;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author Przemek
 */
public class NutritionUnitResource extends ResourceSupport {
    
    private NutritionUnit nutritionUnit;
    
    public NutritionUnitResource (){};
    public NutritionUnitResource(NutritionUnit nutritionUnit){
        this.nutritionUnit = nutritionUnit;
    }

    public NutritionUnit getNutritionUnit() {
        return nutritionUnit;
    }

    public void setNutritionUnit(NutritionUnit nutritionUnit) {
        this.nutritionUnit = nutritionUnit;
    }
    
    
}
