/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service;

import gotogoal.model.FoodProduct;
import gotogoal.repository.FoodProductRepository;
import gotogoal.rest.resource.FoodProductResource;
import gotogoal.rest.resource.assembler.FoodProductAssembler;
import java.util.Arrays;
import java.util.Collection;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Przemek
 */

@Service
public class FoodProductService{

    private FoodProductRepository foodProductRepository;
    private FoodProductAssembler foodProductAssembler;

    @Autowired
    public FoodProductService(FoodProductRepository foodProductRepository, FoodProductAssembler foodProductAssembler) {
        this.foodProductRepository = foodProductRepository;
        this.foodProductAssembler = foodProductAssembler;
    }
    
    public Collection<FoodProductResource> mapToResource(Collection<FoodProduct> foodProducts) {
        return Arrays.asList(foodProducts.stream()
                .map(foodProductAssembler::toResource)
                .toArray(FoodProductResource[]::new));
    }
    
    public FoodProductResource mapToResource(FoodProduct fooProduct){
        return foodProductAssembler.toResource(fooProduct);
    }
     
    public FoodProduct findOne(Long id){
        return foodProductRepository.findOne(id);
    }
    
    
    public FoodProduct save(FoodProduct foodProduct){
        return foodProductRepository.save(foodProduct);
    }
   
    public void delete(Long id){
        foodProductRepository.delete(id);
    }
    
    public Collection<FoodProduct> findAll(){
        return foodProductRepository.findAll();
    }
}
