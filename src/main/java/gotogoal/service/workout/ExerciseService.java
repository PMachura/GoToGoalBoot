/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service.workout;

import gotogoal.model.workout.Exercise;
import gotogoal.repository.workout.ExerciseRepository;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Przemek
 */
@Service
public class ExerciseService {
    
    private ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }
    
    public Collection<Exercise> findAll(){
        return exerciseRepository.findAll();
    }
    
    
}
