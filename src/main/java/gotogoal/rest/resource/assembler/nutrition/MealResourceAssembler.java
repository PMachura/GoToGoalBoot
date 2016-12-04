/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.assembler.nutrition;

import gotogoal.model.nutrition.Meal;
import gotogoal.rest.controller.nutrition.MealRestController;
import gotogoal.rest.controller.nutrition.NutritionDayRestController;
import gotogoal.rest.controller.user.UserRestController;
import gotogoal.rest.resource.nutrition.NutritionDayResource;
import gotogoal.rest.resource.nutrition.MealResource;
import java.util.Arrays;
import java.util.Collection;
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
        mealResource.add(linkTo(MealRestController.class,meal.getNutritionDay().getUser().getId(), meal.getNutritionDay().getId()).slash(meal.getId()).withSelfRel());
        mealResource.add(linkTo(NutritionDayRestController.class,meal.getNutritionDay().getUser().getId()).slash(meal.getNutritionDay().getId()).withRel("nutritionDay"));
        mealResource.add(linkTo(UserRestController.class).slash(meal.getNutritionDay().getUser().getId()).withRel("user"));
        return mealResource;
    }
    
    public Collection<MealResource> toResource(Collection<Meal> meals) {
        return Arrays.asList(meals.stream()
                .map(this::toResource)
                .toArray(MealResource[]::new));
    }
    
}
