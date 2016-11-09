/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller;

import gotogoal.model.FoodProduct;
import gotogoal.rest.resource.FoodProductResource;
import gotogoal.rest.resource.assembler.FoodProductAssembler;
import gotogoal.service.FoodProductService;
import java.net.URI;
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
        FoodProductResource foodProductResource =  foodProductAssembler.toResource(foodProductService.findOne(id));
        return foodProductResource;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public List<FoodProductResource> findAll(){ // tu było Resources<FoodProductResource>
        List<FoodProductResource> foodProductResourceList = foodProductService.findAll()
                .stream()
                .map(foodProduct -> foodProductAssembler.toResource(foodProduct))
                .collect(Collectors.toList());
        
        //return new Resources<FoodProductResource>(foodProductResourceList);
        return foodProductResourceList;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<FoodProduct> create(@RequestBody FoodProduct foodProduct){
        
        FoodProduct entity = foodProductService.save(foodProduct);
        FoodProductResource foodProductResource = foodProductAssembler.toResource(entity);
        HttpHeaders httpHeaders = new HttpHeaders();
        Link linkForCreatedEntity = foodProductResource.getLink("self");
        httpHeaders.setLocation(URI.create(linkForCreatedEntity.getHref()));
        return new ResponseEntity<FoodProduct>(entity, httpHeaders, HttpStatus.CREATED);
    }
    
   
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<FoodProduct> update(@RequestBody FoodProduct foodProduct){
        foodProductService.save(foodProduct);
        return new ResponseEntity<>(foodProduct, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<FoodProduct> delete(@PathVariable Long id){
        foodProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
