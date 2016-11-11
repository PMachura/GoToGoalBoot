/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller;

import gotogoal.model.NutritionDay;
import gotogoal.model.NutritionUnit;
import gotogoal.model.NutritionUnitFoodProduct;
import gotogoal.model.User;
import gotogoal.service.NutritionDayService;
import gotogoal.service.NutritionUnitFoodProductService;
import gotogoal.service.NutritionUnitService;
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

    NutritionUnitService nutritionUnitService;
    NutritionUnitFoodProductService nutritionUnitFoodProductService;
    NutritionDayService nutritionDayService;

    @Autowired
    public TestRestController(NutritionUnitService nutritionUnitService, 
            NutritionUnitFoodProductService nutritionUnitFoodProductService,
            NutritionDayService nutrtiionDayService) {
        this.nutritionUnitService = nutritionUnitService;
        this.nutritionUnitFoodProductService = nutritionUnitFoodProductService;
        this.nutritionDayService = nutrtiionDayService;
    }
    
    @RequestMapping("/daySave")
    public String daySave(){
        NutritionDay nutritionDay = new NutritionDay();
        nutritionDay.setDate(LocalDate.now().plusDays(30));
        nutritionDay.setUser(new User());
        nutritionDay.getUser().setId(new Long(1));
        
    
        List<NutritionUnit> nutritionUnits = new ArrayList<NutritionUnit>();
       
        NutritionUnit nutritionUnit = new NutritionUnit();
    //    nutritionUnit.setLocalDateTime(LocalDateTime.now());
        nutritionUnits.add(nutritionUnit);
        
        nutritionUnit = new NutritionUnit();
   //     nutritionUnit.setLocalDateTime(LocalDateTime.now().plusDays(15));
        nutritionUnits.add(nutritionUnit);
        
        nutritionDay.setNutritionUnits(nutritionUnits);
        nutritionDayService.save(nutritionDay);
        
        
        return "Hello";
    }

    @RequestMapping("/nutritionUnit/{id}")
    public Resource<NutritionUnit> getNutritionUnit(@PathVariable Long id) {
        NutritionUnit nutritionUnit = nutritionUnitService.findOne(id);
        List<NutritionUnitFoodProduct> nutritionUnitFoodProductList = nutritionUnitFoodProductService.findByNutritionUnitId(id);
        nutritionUnitFoodProductList.forEach((NutritionUnitFoodProduct nufp) -> System.out.println("@@@@@" + nufp.getId()));
        nutritionUnit.setNutritionUnitsFoodProducts(nutritionUnitFoodProductList);
        return new Resource(nutritionUnit);

    }

    @RequestMapping(value = "nutritionUnit", method = RequestMethod.POST)
    public String createNutritionUnit(@RequestBody NutritionUnit nutritionUnit) {
       // NutritionUnit saved = nutritionUnitService.save(nutritionUnit);
 ///     System.out.println("DateTime " + nutritionUnit.getLocalDateTime());
       System.out.println("Nutrition Unit id " + nutritionUnit.getNutritionDay().getId());
       for(NutritionUnitFoodProduct nufp : nutritionUnit.getNutritionUnitsFoodProducts()){
           System.out.println("grams " + nufp.getGrams());
       }
      //  return nutritionUnit;
      return "OK";

    }
    
    @RequestMapping(value = "nutritionUnit2", method = RequestMethod.POST)
    public NutritionUnit createNutritionUnit2(@RequestBody NutritionUnit nutritionUnit) {
       // NutritionUnit saved = nutritionUnitService.save(nutritionUnit);
    //   System.out.println("DateTime " + nutritionUnit.getLocalDateTime());
       System.out.println("Nutrition Unit id " + nutritionUnit.getNutritionDay().getId());
       for(NutritionUnitFoodProduct nufp : nutritionUnit.getNutritionUnitsFoodProducts()){
           System.out.println("grams " + nufp.getGrams());
       }
       return nutritionUnit;
    }

}
