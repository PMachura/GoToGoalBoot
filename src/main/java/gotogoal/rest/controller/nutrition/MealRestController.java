/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller.nutrition;

import gotogoal.model.nutrition.FoodProduct;
import gotogoal.model.nutrition.Meal;
import gotogoal.model.nutrition.MealFoodProduct;
import gotogoal.rest.resource.nutrition.MealResource;
import gotogoal.rest.resource.assembler.nutrition.MealResourceAssembler;
import gotogoal.rest.resource.nutrition.NutritionDayResource;
import gotogoal.service.nutrition.MealService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Przemek
 */
@RestController
@RequestMapping("/api/users/{userId}/nutritionDays/{nutritionDayId}/meals")
public class MealRestController {

    MealService mealService;
    MealResourceAssembler mealResourceAssembler;

    @Autowired
    public MealRestController(MealService mealService, MealResourceAssembler mealResoruceAssembler) {
        this.mealService = mealService;
        this.mealResourceAssembler = mealResoruceAssembler;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<MealResource>> findAll(@PathVariable Long userId, @PathVariable Long nutritionDayId) {
        Collection<MealResource> response = mealResourceAssembler.toResource(mealService.findByUserIdAndNutritionDayIdEager(userId, nutritionDayId));
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<MealResource> findOne(@PathVariable Long id){
        return new ResponseEntity(mealResourceAssembler.toResource(mealService.findOneEager(id)), HttpStatus.OK);
    }
   
}
