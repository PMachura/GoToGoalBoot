/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.assembler.nutrition;

import gotogoal.model.nutrition.Meal;
import gotogoal.rest.controller.nutrition.MealRestController;
import gotogoal.rest.resource.nutrition.NutritionDayResource;
import gotogoal.rest.resource.nutrition.MealResource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 *
 * @author Przemek
 */
@Component
public class MealResourceAssembler extends ResourceAssemblerSupport<Meal,MealResource>{
    
    public MealResourceAssembler(){
        super(MealRestController.class, MealResource.class);
    }
    
    @Override
    public MealResource toResource(Meal meal){
        MealResource mealResource = new MealResource(meal);
        mealResource.add(linkTo(MealRestController.class,meal.getNutritionDay().getUser().getEmail(), meal.getNutritionDay().getDate()).slash(meal.getId()).withSelfRel());
        return mealResource;
    }
    
//    @Override
//    protected MealResource instantiateResource(Meal meal){
//        return new MealResource(meal);
//    }
}
