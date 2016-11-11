/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller;

import gotogoal.service.NutritionUnitFoodProductService;
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
@RequestMapping("/api/users/{userEmail}/nutritionDays/{nutritionDayDate}/nutritionUnits/{nutritionUnitId}/nutritionUnitsFoodProducts")
public class NutritionUnitFoodProductRestController {
    
    NutritionUnitFoodProductService nutritionUnitFoodProductService;
    
    @Autowired
    public NutritionUnitFoodProductRestController(NutritionUnitFoodProductService nutritionUnitFoodProductService){
      this.nutritionUnitFoodProductService = nutritionUnitFoodProductService;  
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id){
        nutritionUnitFoodProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK); 
    }
}
