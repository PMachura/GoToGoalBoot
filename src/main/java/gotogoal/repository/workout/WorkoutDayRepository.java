/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.repository.workout;

import gotogoal.model.workout.WorkoutDay;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Przemek
 */
public interface WorkoutDayRepository extends JpaRepository<WorkoutDay, Long> {
    public Page<WorkoutDay> findByUserId(Long userId, Pageable pageable);
    public WorkoutDay findByUserIdAndId(Long userId, Long id);
    public WorkoutDay findByUserIdAndDate(Long userId, LocalDate date);
    public Page<WorkoutDay> findByUserIdAndDateLessThanEqual(Long userId, LocalDate date, Pageable pageable);
}
