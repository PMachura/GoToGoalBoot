/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.repository.workout;

import gotogoal.model.workout.Exercise;
import gotogoal.model.workout.WorkoutExercise;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Przemek
 */
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long>  {
    public Collection<WorkoutExercise> findByWorkoutId(Long workoutId);
    public void deleteByWorkoutWorkoutDayIdInAndWorkoutIdNotIn(Long workoutDayId, Collection<Long> workoutIds);    
    public void deleteByWorkoutId(Long id);
    public void deleteByWorkoutIdInAndIdNotIn(Long id ,Collection<Long> ids);
    public void deleteByWorkoutWorkoutDayId(Long workoutDayId);
}
