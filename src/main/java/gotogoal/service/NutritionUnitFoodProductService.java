/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service;

import gotogoal.model.NutritionUnitFoodProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gotogoal.repository.NutritionUnitFoodProductRepository;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Przemek
 */
@Service
public class NutritionUnitFoodProductService {
    
    private NutritionUnitFoodProductRepository nutritionUnitFoodProductRepository;
    
    @Autowired
    public NutritionUnitFoodProductService(NutritionUnitFoodProductRepository foodProductNutritionUnitRepository){
        this.nutritionUnitFoodProductRepository = foodProductNutritionUnitRepository;
    }
    
    public List<NutritionUnitFoodProduct> findByNutritionUnitId(Long nutritionUnitId){
        return nutritionUnitFoodProductRepository.findByNutritionUnitId(nutritionUnitId);
    }
    
    public Collection<NutritionUnitFoodProduct> findByFoodProductId(Long foodProductId){
        return nutritionUnitFoodProductRepository.findByFoodProductId(foodProductId);
    }
    
    public NutritionUnitFoodProduct findOne(Long id){
        return nutritionUnitFoodProductRepository.findOne(id);
    }
    
    public void delete(Long id){
        nutritionUnitFoodProductRepository.delete(id);
    }
}
