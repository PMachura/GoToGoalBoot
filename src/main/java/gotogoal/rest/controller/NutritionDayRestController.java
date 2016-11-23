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
@RequestMapping("/api/users/{userId}/nutritionDays")
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

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<NutritionDayResource>> findAll(
            @PathVariable Long userId,
            @RequestParam(value = "date", required = false) LocalDate date,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "desc") String sort) {

        Page<NutritionDayResource> nutritionDayResourcePage = 
                nutritionDayService.mapToResource(nutritionDayService.findAllByUserIdEager(userId, new PageRequest(page, size, new Sort(Sort.Direction.fromStringOrNull(sort), "date"))));
        
        return new ResponseEntity(nutritionDayResourcePage, HttpStatus.OK);
    }

    /**
     *
     * @param nutritionDay
     * @param userEmail
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<NutritionDayResource> create(@RequestBody NutritionDay nutritionDay, @PathVariable Long userId) {

        for (Meal meal : nutritionDay.getMeals()) {
            mealService.temporaryDebug("", meal);
        }

        NutritionDay created = nutritionDayService.create(nutritionDay, userId);
        NutritionDayResource response = nutritionDayService.mapToResource(nutritionDayService.findOneEagerDeep(created.getId()));

        return new ResponseEntity<NutritionDayResource>(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long userId, @PathVariable Long id) {
        nutritionDayService.delete(userId, id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<NutritionDayResource> update(@RequestBody NutritionDay nutritionDay,
            @PathVariable Long userId,
            @PathVariable Long id) {
        NutritionDay updated = nutritionDayService.update(nutritionDay, userId, id);
        NutritionDayResource response = nutritionDayService.mapToResource(nutritionDayService.findOneEagerDeep(updated.getId()));
        return new ResponseEntity<NutritionDayResource>(response, HttpStatus.OK);
    }

}
