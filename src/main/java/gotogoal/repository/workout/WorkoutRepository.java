/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.repository.workout;

import gotogoal.model.workout.Workout;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Przemek
 */
public interface WorkoutRepository extends JpaRepository<Workout, Long>  {
    public Collection<Workout> findByWorkoutDayUserIdAndWorkoutDayId(Long userId, Long workoutDayId);
    public Collection<Workout> findByWorkoutDayId(Long workoutDayId);
    public void deleteByWorkoutDayId(Long workoutDayId);
    public void deleteByWorkoutDayIdInAndIdNotIn(Long workoutDayId,Collection<Long> workoutsIds);
    
}
