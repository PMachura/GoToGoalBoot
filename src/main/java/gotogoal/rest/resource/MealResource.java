/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource;

import gotogoal.model.Meal;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author Przemek
 */
public class MealResource extends ResourceSupport {
    
    private Meal meal;
    
    public MealResource (){};
    public MealResource(Meal meal){
        this.meal = meal;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }
    
    
}
