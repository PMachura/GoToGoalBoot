/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller;

import gotogoal.service.MealFoodProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Przemek
 */
@RestController
@RequestMapping("/api/users/{userEmail}/nutritionDays/{nutritionDayDate}/meals/{mealId}/mealsFoodProducts")
public class MealFoodProductRestController {
    
    MealFoodProductService mealFoodProductService;
    
    @Autowired
    public MealFoodProductRestController(MealFoodProductService mealFoodProductService){
      this.mealFoodProductService = mealFoodProductService;  
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id){
        mealFoodProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK); 
    }
    
   
}
