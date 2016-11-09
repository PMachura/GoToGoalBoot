/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.repository;

import gotogoal.model.NutritionUnit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Przemek
 */
public interface NutritionUnitRepository extends JpaRepository<NutritionUnit, Long> {

    public Collection<NutritionUnit> findByNutritionDayId(Long nutritionDiaryId);
    public Page<NutritionUnit> findByNutritionDayId(Long nutritionDiaryId, Pageable pageable);
    public Collection<NutritionUnit> findBylocalDateTimeGreaterThan(LocalDateTime localDateTime);
    public Collection<NutritionUnit> findByNutritionDayUserEmailAndNutritionDayDate(String userEmail, LocalDate localDate);

}
