/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Przemek
 */
@RestController
@RequestMapping("/api/users/{userEmail}/nutritionDays/{nutritionDayDate}/nutritionUnits/{nutritionUnitId}/nutritionUnitFoodProducts")
public class NutritionUnitFoodProductRestController {
    
   // @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
}
