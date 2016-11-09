/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import gotogoal.model.NutritionDay;
import gotogoal.model.NutritionUnit;

import gotogoal.rest.resource.NutritionDayResource;
import gotogoal.rest.resource.assembler.NutritionDayResourceAssembler;
import gotogoal.rest.resource.assembler.NutritionUnitResourceAssembler;
import gotogoal.service.NutritionDayService;
import gotogoal.service.NutritionUnitService;
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
    NutritionUnitService nutritionUnitService;
    NutritionDayResourceAssembler nutritionDayResourceAssembler;
   
    @Autowired
    public NutritionDayRestController(NutritionDayService nutritionDiaryService, NutritionUnitService nutritionUnitService,
            NutritionDayResourceAssembler dailyNutritionResourceAssembler) {
        this.nutritionDayService = nutritionDiaryService;
        this.nutritionUnitService = nutritionUnitService;
        this.nutritionDayResourceAssembler = dailyNutritionResourceAssembler;
    }
    
    //TEST
    @RequestMapping(value = "/less", method = RequestMethod.GET)
    public  Page<NutritionDay> findAllDesc(
            @RequestParam(value="date", required = false) LocalDate date,
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value="size", required = false, defaultValue = "3") int size){  
        return this.nutritionDayService.findAllEagerAsResourceDateLess(date, 
                new PageRequest(page,size, new Sort(Sort.Direction.ASC, "date")));
    }
    
      //TEST
    @RequestMapping(value = "/less2", method = RequestMethod.GET)
    public  Page<NutritionDay> findAllDesc2(
            @RequestParam(value="date", required = false) LocalDate date,
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value="size", required = false, defaultValue = "3") int size){  
        return this.nutritionDayService.findAllEagerAsResourceDateLess(date, 
                new PageRequest(page,size, new Sort(Sort.Direction.DESC, "date")));
    }
    
     
    //Tutaj dla size = 1 trzeba w serwisie przerobić, żeby traktowało jako pojedyncy obiekt i szukało po dacie o ile jest 
    @RequestMapping(method = RequestMethod.GET)
    public Page<NutritionDay> findAll(
            @PathVariable String userEmail,
            @RequestParam(value="date", required = false) LocalDate date,
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value="size", required = false, defaultValue = "10") int size,
            @RequestParam(value="sort", required = false, defaultValue = "desc") String sort){  
        return this.nutritionDayService.findAllLazy
        (userEmail, date, new PageRequest(page,size, new Sort(Sort.Direction.fromStringOrNull(sort), "date")));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<NutritionDayResource> findOne(
            @PathVariable String userEmail,
            @PathVariable Long id) {
        return new ResponseEntity<NutritionDayResource>(nutritionDayService.findOneEagerAsResource(id), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<NutritionDayResource> create(@RequestBody NutritionDay nutritionDay){
        NutritionDay savedNutritionDay = nutritionDayService.save(nutritionDay);
        NutritionDayResource nutritionDayResource = nutritionDayResourceAssembler.toResource(savedNutritionDay);
        return new ResponseEntity<>(nutritionDayResource,HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<NutritionDayResource> update(@RequestBody NutritionDay nutritionDay){
        NutritionDay savedNutritionDay = nutritionDayService.update(nutritionDay);
        NutritionDayResource nutritionDayResource = nutritionDayResourceAssembler.toResource(savedNutritionDay);
        return new ResponseEntity<>(nutritionDayResource,HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id){
        nutritionDayService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
   

}
