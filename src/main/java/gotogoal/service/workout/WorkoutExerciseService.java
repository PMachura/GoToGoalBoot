/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service.workout;

import gotogoal.model.workout.WorkoutExercise;
import gotogoal.repository.workout.WorkoutExerciseRepository;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Przemek
 */
@Service
public class WorkoutExerciseService {
    
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    public WorkoutExerciseService(WorkoutExerciseRepository workoutExerciseRepository) {
        this.workoutExerciseRepository = workoutExerciseRepository;
    }
    
    public Collection<WorkoutExercise> findByWorkoutId(Long workoutId){
        return workoutExerciseRepository.findByWorkoutId(workoutId);
    }
    
    @Transactional
    public Collection<WorkoutExercise> save(Collection<WorkoutExercise> workoutExercises){
        return workoutExerciseRepository.save(workoutExercises);
    }
    
    public void deleteByWorkoutWorkoutDayIdInAndWorkoutIdNotIn(Long workoutDayId, Collection<Long> workoutIds){
        workoutExerciseRepository.deleteByWorkoutWorkoutDayIdInAndWorkoutIdNotIn(workoutDayId, workoutIds);
    }
    
    public void deleteByWorkoutId(Long id){
        workoutExerciseRepository.deleteByWorkoutId(id);
    }
    public void deleteByWorkoutIdInAndIdNotIn(Long id ,Collection<Long> ids){
        workoutExerciseRepository.deleteByWorkoutIdInAndIdNotIn(id, ids);
    }

    void deleteByWorkoutWorkoutDayId(Long workoutDayId) {
       workoutExerciseRepository.deleteByWorkoutWorkoutDayId(workoutDayId);
    }
}
