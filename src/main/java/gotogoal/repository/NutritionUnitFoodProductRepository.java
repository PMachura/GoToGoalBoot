/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.repository;

import gotogoal.model.NutritionUnitFoodProduct;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Przemek
 */
public interface NutritionUnitFoodProductRepository extends JpaRepository<NutritionUnitFoodProduct, Long> {
    
    public List<NutritionUnitFoodProduct> findByNutritionUnitId(Long nutritionUnitId);
    public Collection<NutritionUnitFoodProduct> findByFoodProductId(Long foodProductId);
}
