/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.nutrition;

import gotogoal.model.nutrition.NutritionDay;
import java.util.List;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author Przemek
 */

public class NutritionDayResource extends ResourceSupport {
    
    private NutritionDay nutritionDay;
    private List<NutritionDay> nutritionDays;
    
    public NutritionDayResource(){};
    public NutritionDayResource(NutritionDay nutritionDay){
        this.nutritionDay = nutritionDay;
        this.nutritionDays = null;
    }
    public NutritionDayResource(List<NutritionDay> nutritionDays){
        this.nutritionDays = nutritionDays;
        this.nutritionDay = null;
    }
    
    public NutritionDay getNutritionDay() {
        return nutritionDay;
    }

    public void setNutritionDay(NutritionDay nutritionDay) {
        this.nutritionDay = nutritionDay;
    }

    public List<NutritionDay> getNutritionDays() {
        return nutritionDays;
    }

    public void setNutritionDays(List<NutritionDay> nutritionDays) {
        this.nutritionDays = nutritionDays;
    }
    
    
}
