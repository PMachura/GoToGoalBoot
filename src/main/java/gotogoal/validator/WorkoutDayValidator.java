/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.validator;

import gotogoal.exception.EntityDuplicateException;
import gotogoal.exception.EntityNotFoundException;
import gotogoal.exception.EntityRelationException;
import gotogoal.exception.ForbiddenResourcesException;
import gotogoal.exception.PathAndModelDataMismatchException;
import gotogoal.model.workout.WorkoutDay;
import gotogoal.model.user.User;
import gotogoal.model.workout.Workout;
import gotogoal.repository.workout.WorkoutDayRepository;
import gotogoal.repository.user.UserRepository;
import java.security.Principal;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Przemek
 */
@Component
public class WorkoutDayValidator {
    private UserRepository userRepository;
    private WorkoutDayRepository workoutDayRepository;

    @Autowired
    public WorkoutDayValidator(UserRepository userRepository, WorkoutDayRepository workoutDayRepository) {
        this.userRepository = userRepository;
        this.workoutDayRepository = workoutDayRepository;
    }
    
    public void validateAuthorization(Principal principal, Long pathUserId){
        if(principal == null || userRepository.findByEmail(principal.getName()).getId() != pathUserId) throw new ForbiddenResourcesException("Próbujesz dostać się do zasobów innego użytkownika");
    }
    
    public void validateCompabilityOfEntityRelationData(WorkoutDay workoutDay){
        if(workoutDay.getWorkouts() != null){
            Collection<Workout> workouts = workoutDay.getWorkouts();
            workouts.forEach((Workout workout) -> {
                if(workout.getWorkoutDay() != null && workout.getWorkoutDay().getId() != null && workout.getWorkoutDay().getId() != workoutDay.getId()){
                    throw new EntityRelationException("Posiłki deklarują przynależność do innego dziennika żywieniowego");
                }
            });
        }
    }
    
    public void validateNewEntityUniqueness(WorkoutDay workoutDay, Long userId){
        if(workoutDayRepository.findByUserIdAndDate(userId, workoutDay.getDate()) !=null)
            throw new EntityDuplicateException("Podany dziennik już istnieje");
    }
    
    public void validateUpdatedEntityUniqueness(WorkoutDay workoutDay, Long userId, Long workoutDayId){
        WorkoutDay currentWorkoutDay = workoutDayRepository.findOne(workoutDayId);
        if(!currentWorkoutDay.getDate().equals(workoutDay.getDate())){
            validateNewEntityUniqueness(workoutDay, userId);
        }
        
    }
    
    public void validateCompatibilityOfPathAndModelData(WorkoutDay workoutDay, Long pathUserId){
        if(workoutDay.getUser() != null && workoutDay.getUser().getId() != null && workoutDay.getUser().getId() != pathUserId ) 
            throw new PathAndModelDataMismatchException("Przesłany dziennik żywieniowy deklaruje przynależność do innego użytkownika, niż określa to ścieżka żądania");
    }
    
    public void validateCompatibilityOfPathAndModelData(WorkoutDay workoutDay, Long pathUserId, Long pathWorkoutDayId){
        validateCompatibilityOfPathAndModelData(workoutDay, pathUserId);
        if(workoutDay.getId() != null && workoutDay.getId() != pathWorkoutDayId )
             throw new PathAndModelDataMismatchException("Przesłany dziennik żywieniowy deklaruje inny identyfikator, niż określa to ścieżka żądania");
    }
    
    public void validateEntityExistance(Long pathUserId, Long pathWorkoutDayId){
        if(workoutDayRepository.findByUserIdAndId(pathUserId, pathWorkoutDayId) == null)
            throw new EntityNotFoundException("Dziennik, który chcesz zaktualizować nie istnieje");
    }
    
    public void validateWorkoutDayCreation(WorkoutDay workoutDay, Long pathUserId, Principal principal ){
        validateAuthorization(principal, pathUserId);
        validateCompatibilityOfPathAndModelData(workoutDay, pathUserId);
        validateNewEntityUniqueness(workoutDay, pathUserId);
    }
    
    public void validateWorkoutDayUpdates(WorkoutDay workoutDay, Long pathUserId, Long pathWorkoutDayId, Principal principal ){
        validateAuthorization(principal, pathUserId);
        validateEntityExistance(pathUserId, pathWorkoutDayId);
        validateCompatibilityOfPathAndModelData(workoutDay, pathUserId, pathWorkoutDayId);
        validateUpdatedEntityUniqueness(workoutDay, pathUserId, pathWorkoutDayId);
    }
    
    public void validateWorkouDayDeletion(Long pathUserId, Long pathWorkoutDayId, Principal principal ){
        validateAuthorization(principal, pathUserId);
        validateEntityExistance(pathUserId, pathWorkoutDayId);
        
    }
}
