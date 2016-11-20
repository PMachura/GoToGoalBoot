/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service;

import gotogoal.model.Meal;
import gotogoal.model.MealFoodProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gotogoal.repository.MealFoodProductRepository;
import java.util.Collection;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Przemek
 */
@Service
@Transactional
public class MealFoodProductService {
    
    private MealFoodProductRepository mealFoodProductRepository;
    
    @Autowired
    public MealFoodProductService(MealFoodProductRepository foodProductMealRepository){
        this.mealFoodProductRepository = foodProductMealRepository;
    }
    
    public List<MealFoodProduct> findByMealId(Long mealId){
        return mealFoodProductRepository.findByMealId(mealId);
    }
    
    public Collection<MealFoodProduct> findByFoodProductId(Long foodProductId){
        return mealFoodProductRepository.findByFoodProductId(foodProductId);
    }
    
    public MealFoodProduct findOne(Long id){
        return mealFoodProductRepository.findOne(id);
    }
    
    public void delete(Long id){
        mealFoodProductRepository.delete(id);
    }
    
    public Collection<MealFoodProduct> deleteByMealIdInAndIdNotIn(Long mealId, Collection<Long> ids){
        return mealFoodProductRepository.deleteByMealIdInAndIdNotIn(mealId, ids);
    }
    
    @Transactional
    public Collection<MealFoodProduct> save(Collection<MealFoodProduct> mealsFoodProducts){
        return mealFoodProductRepository.save(mealsFoodProducts);
    }
    
    public void deleteByMealId(Long mealId){
         mealFoodProductRepository.deleteByMealId(mealId);
    }
    
    public void deleteByMealNutritionDayId(Long nutritionDayId){
        mealFoodProductRepository.deleteByMealNutritionDayId(nutritionDayId);
    }
    
    public void deleteByMealNutritionDayIdInAndMealIdNotIn(Long nutritionDayId, Collection<Long> melsIds){
        mealFoodProductRepository.deleteByMealNutritionDayIdInAndMealIdNotIn(nutritionDayId, melsIds);
    }
}
