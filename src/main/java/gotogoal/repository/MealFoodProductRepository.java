/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.repository;

import gotogoal.model.Meal;
import gotogoal.model.MealFoodProduct;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Przemek
 */
public interface MealFoodProductRepository extends JpaRepository<MealFoodProduct, Long> {
    
    public List<MealFoodProduct> findByMealId(Long mealId);
    public Collection<MealFoodProduct> findByFoodProductId(Long foodProductId);
    public Collection<MealFoodProduct> deleteByMealIdInAndIdNotIn(Long mealId, Collection<Long> ids);
    public void deleteByMealId(Long mealId);
    public void deleteByMealNutritionDayId(Long nutritionDayId);
    public void deleteByMealNutritionDayIdInAndMealIdNotIn(Long nutritionDayId, Collection<Long> melsIds);
}
