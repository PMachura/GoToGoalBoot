/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller.nutrition;

import gotogoal.model.nutrition.NutritionDay;
import gotogoal.model.nutrition.Meal;
import gotogoal.model.nutrition.MealFoodProduct;
import gotogoal.model.user.User;
import gotogoal.service.nutrition.NutritionDayService;
import gotogoal.service.nutrition.MealFoodProductService;
import gotogoal.service.nutrition.MealService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Przemek
 */
@RestController
@RequestMapping("/api/test")
public class TestRestController {

    MealService mealService;
    MealFoodProductService mealFoodProductService;
    NutritionDayService nutritionDayService;

    @Autowired
    public TestRestController(MealService mealService, 
            MealFoodProductService mealFoodProductService,
            NutritionDayService nutrtiionDayService) {
        this.mealService = mealService;
        this.mealFoodProductService = mealFoodProductService;
        this.nutritionDayService = nutrtiionDayService;
    }
  
}
