/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.repository;

import gotogoal.model.NutritionDay;
import gotogoal.model.NutritionUnit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Przemek
 */

public interface NutritionDayRepository extends JpaRepository<NutritionDay, Long> {
    public NutritionDay findByUserEmail(String userEmail);
    public Page<NutritionDay> findByDateGreaterThanEqual(LocalDate localDate, Pageable pageable);
    public Page<NutritionDay> findByUserEmailAndDateGreaterThanEqual(String userEmail,LocalDate localDate, Pageable pageable);
    public NutritionDay findByUserEmailAndDate(String userEmail, LocalDate localDate);
    public Page<NutritionDay> findByDateLessThanEqual(LocalDate localDate, Pageable pageable);
    public Page<NutritionDay> findByUserEmail(String userEmail, Pageable pageable);
}
