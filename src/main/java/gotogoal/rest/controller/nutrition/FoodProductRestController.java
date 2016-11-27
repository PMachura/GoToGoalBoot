/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller.nutrition;

import gotogoal.model.nutrition.FoodProduct;
import gotogoal.rest.resource.nutrition.FoodProductResource;
import gotogoal.rest.resource.assembler.nutrition.FoodProductAssembler;
import gotogoal.service.nutrition.FoodProductService;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/foodProducts")
public class FoodProductRestController {
    
    private FoodProductService foodProductService;
    private FoodProductAssembler foodProductAssembler;
    
    @Autowired
    FoodProductRestController(FoodProductService foodProductService, FoodProductAssembler foodProductAssembler){
        this.foodProductService = foodProductService;
        this.foodProductAssembler = foodProductAssembler;
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public FoodProductResource show(@PathVariable Long id){
        FoodProductResource foodProductResource =  foodProductService.mapToResource(foodProductService.findOne(id));
        return foodProductResource;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<FoodProductResource>> findAll(){ 
        Collection<FoodProductResource> foodProductsResources = foodProductService.mapToResource(foodProductService.findAll());
         
        return new ResponseEntity(foodProductsResources, HttpStatus.OK);
    }
        
}
