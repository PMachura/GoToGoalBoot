/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.repository;

import gotogoal.model.Meal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Przemek
 */
public interface MealRepository extends JpaRepository<Meal, Long> {

    public Collection<Meal> findByNutritionDayId(Long nutritionDiaryId);
    public Page<Meal> findByNutritionDayId(Long nutritionDiaryId, Pageable pageable);
    public Collection<Meal> findByTimeGreaterThan(LocalTime localTime);
    public Collection<Meal> findByNutritionDayUserEmailAndNutritionDayDate(String userEmail, LocalDate localDate);
    public void deleteByNutritionDayId(Long mealId);
    public void deleteByNutritionDayIdInAndIdNotIn(Long nutritionDayId, Collection<Long> melsIds);
    public Collection<Meal> findByNutritionDayUserIdAndNutritionDayId(Long userId, Long nutritionDayId);

}
