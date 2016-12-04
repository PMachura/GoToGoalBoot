/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.repository.workout;

import gotogoal.model.nutrition.NutritionDay;
import gotogoal.model.workout.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Przemek
 */
public interface ExerciseRepository  extends JpaRepository<Exercise, Long>{
    
}
