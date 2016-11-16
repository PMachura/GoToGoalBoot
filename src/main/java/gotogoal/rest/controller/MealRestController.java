/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller;

import gotogoal.model.FoodProduct;
import gotogoal.model.Meal;
import gotogoal.model.MealFoodProduct;
import gotogoal.rest.resource.MealResource;
import gotogoal.rest.resource.assembler.MealResourceAssembler;
import gotogoal.service.MealService;
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
@RequestMapping("/api/users/{userEmail}/nutritionDays/{nutritionDayDate}/meals")
public class MealRestController {

    MealService mealService;
    MealResourceAssembler mealResourceAssembler;

    @Autowired
    public MealRestController(MealService mealService, MealResourceAssembler mealResoruceAssembler) {
        this.mealService = mealService;
        this.mealResourceAssembler = mealResoruceAssembler;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MealResource>> findAll(@PathVariable String userEmail, @PathVariable LocalDate nutritionDayDate) {
        return new ResponseEntity(mealService.findAllByUserEmailAndNutritionDayDateAsResourceEager(userEmail, nutritionDayDate), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Meal> update(@RequestBody Meal meal) {

        Meal updated = mealService.update(meal);      
        return new ResponseEntity<Meal>((mealService.findOneEager(updated.getId())), HttpStatus.ACCEPTED);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Meal> create(@RequestBody Meal meal) {
        Meal created = mealService.create(meal);      
        return new ResponseEntity<Meal>((mealService.findOneEager(created.getId())), HttpStatus.ACCEPTED);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id){
        mealService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
