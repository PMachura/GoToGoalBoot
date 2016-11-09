/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service;

import gotogoal.model.FoodProduct;
import gotogoal.repository.FoodProductRepository;

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

    @Autowired
    public FoodProductService(FoodProductRepository foodProductRepository) {
        this.foodProductRepository = foodProductRepository;
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
    
    public List<FoodProduct> findAll(){
        return foodProductRepository.findAll();
    }
}
