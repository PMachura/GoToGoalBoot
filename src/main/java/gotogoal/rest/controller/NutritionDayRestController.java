/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import gotogoal.model.NutritionDay;
import gotogoal.model.Meal;

import gotogoal.rest.resource.NutritionDayResource;
import gotogoal.rest.resource.assembler.NutritionDayResourceAssembler;
import gotogoal.rest.resource.assembler.MealResourceAssembler;
import gotogoal.service.NutritionDayService;
import gotogoal.service.MealService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/api/users/{userEmail}/nutritionDays")
public class NutritionDayRestController {

    NutritionDayService nutritionDayService;
    MealService mealService;
    NutritionDayResourceAssembler nutritionDayResourceAssembler;
   
    @Autowired
    public NutritionDayRestController(NutritionDayService nutritionDiaryService, MealService mealService,
            NutritionDayResourceAssembler nutritionDayResourceAssembler) {
        this.nutritionDayService = nutritionDiaryService;
        this.mealService = mealService;
        this.nutritionDayResourceAssembler = nutritionDayResourceAssembler;
    }
   
     
    //Tutaj dla size = 1 trzeba w serwisie przerobić, żeby traktowało jako pojedyncy obiekt i szukało po dacie o ile jest 
    @RequestMapping(method = RequestMethod.GET)
    public Page<NutritionDay> findAll(
            @PathVariable String userEmail,
            @RequestParam(value="date", required = false) LocalDate date,
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value="size", required = false, defaultValue = "10") int size,
            @RequestParam(value="sort", required = false, defaultValue = "desc") String sort){  
        return this.nutritionDayService.findAllByUserEmailAndDateAsPageLazy
        (userEmail, date, new PageRequest(page,size, new Sort(Sort.Direction.fromStringOrNull(sort), "date")));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<NutritionDayResource> findOne(
            @PathVariable String userEmail,
            @PathVariable Long id) {
        return new ResponseEntity<NutritionDayResource>(nutritionDayService.findOneEagerAsResource(id), HttpStatus.OK);
    }
    
    /**
     *
     * @param nutritionDay
     * @param userEmail
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<NutritionDayResource> create(@RequestBody NutritionDay nutritionDay, @PathVariable String userEmail){
        
        System.out.println("NutritionDay->id " + nutritionDay.getId());
        System.out.println("NutritionDay->date " + nutritionDay.getDate());
        System.out.println("NutritionDay->userEmail " + nutritionDay.getUser().getEmail());
        for(Meal meal : nutritionDay.getMeals()){
            mealService.temporaryDebug("", meal);
        }
        
        NutritionDay created = nutritionDayService.create(nutritionDay, userEmail);
        NutritionDayResource response = nutritionDayService.findOneAsResourceEagerDeep(created.getId());
    
        return new ResponseEntity<NutritionDayResource>(response, HttpStatus.CREATED) ;
    }
    
    @RequestMapping(value = "/{date}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String userEmail, @PathVariable LocalDate date){
        nutritionDayService.delete(userEmail, date);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{date}", method = RequestMethod.PUT)
    public NutritionDay update(@RequestBody NutritionDay nutritionDay){
       return nutritionDayService.update(nutritionDay);
    }
    
}
