/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller;

import gotogoal.model.FoodProduct;
import gotogoal.model.NutritionUnit;
import gotogoal.model.NutritionUnitFoodProduct;
import gotogoal.rest.resource.NutritionUnitResource;
import gotogoal.rest.resource.assembler.NutritionUnitResourceAssembler;
import gotogoal.service.NutritionUnitService;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Przemek
 */
@RestController
@RequestMapping("/api/users/{userEmail}/nutritionDays/{nutritionDayDate}/nutritionUnits")
public class NutritionUnitRestController {

    NutritionUnitService nutritionUnitService;
    NutritionUnitResourceAssembler nutritionUnitResourceAssembler;

    @Autowired
    public NutritionUnitRestController(NutritionUnitService nutritionUnitService, NutritionUnitResourceAssembler nutritionUnitResoruceAssembler) {
        this.nutritionUnitService = nutritionUnitService;
        this.nutritionUnitResourceAssembler = nutritionUnitResoruceAssembler;
    }
    
    @RequestMapping
    public ResponseEntity<List<NutritionUnitResource>> findAll(@PathVariable String userEmail, @PathVariable LocalDate nutritionDayDate){
        return new ResponseEntity(nutritionUnitService.findAllAsResourceEager(userEmail, nutritionDayDate),HttpStatus.OK);
    }

    @RequestMapping("/foodProducts")
    public List<FoodProduct> foodProducts(@PathVariable Long id) {
        NutritionUnit nutritionUnit = nutritionUnitService.findOne(id);
        List<FoodProduct> foodProducts = new ArrayList<FoodProduct>();
        nutritionUnit.getNutritionUnitsFoodProducts()
                .forEach((NutritionUnitFoodProduct nutritionUnitFoodProduct) -> foodProducts.add(nutritionUnitFoodProduct.getFoodProduct()));
        return foodProducts;

    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<NutritionUnitResource> findOne(@PathVariable Long id){
        NutritionUnitResource nutritionUnitResource = nutritionUnitResourceAssembler.toResource(nutritionUnitService.findOne(id));
        nutritionUnitResource.getNutritionUnit().setNutritionUnitsFoodProducts(null);
        return new ResponseEntity<>(nutritionUnitResource,HttpStatus.OK);
    }
    
    @RequestMapping(value="/testPage", method = RequestMethod.GET)
    public Page<NutritionUnit> getPage(@RequestParam(required = false) int page, @RequestParam(required = false) int size){
        Pageable pageable = new PageRequest(page,size);
        return this.nutritionUnitService.testPage(pageable);
    }
    
    @RequestMapping(value="/testDate")
    public Collection<NutritionUnit> getDate(){
        return this.nutritionUnitService.testDateTime(LocalDateTime.now());
    }

}
