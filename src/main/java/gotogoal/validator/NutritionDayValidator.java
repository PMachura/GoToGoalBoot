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
import gotogoal.model.nutrition.Meal;
import gotogoal.model.nutrition.NutritionDay;
import gotogoal.model.user.User;
import gotogoal.model.workout.WorkoutDay;
import gotogoal.repository.nutrition.NutritionDayRepository;
import gotogoal.repository.user.UserRepository;
import gotogoal.service.user.UserService;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Przemek
 */
@Component
public class NutritionDayValidator {
    
    private UserRepository userRepository;
    private NutritionDayRepository nutritionDayRepository;

    @Autowired
    public NutritionDayValidator(UserRepository userRepository, NutritionDayRepository nutritionDayRepository) {
        this.userRepository = userRepository;
        this.nutritionDayRepository = nutritionDayRepository;
    }
    
    public void validateAuthorization(Principal principal, Long pathUserId){    
        if(principal == null || userRepository.findByEmail(principal.getName()).getId() != pathUserId) throw new ForbiddenResourcesException("Próbujesz dostać się do zasobów innego użytkownika");
    }
    
    public void validateCompatibilityOfPathAndModelData(NutritionDay nutritionDay, Long pathUserId){
        if(nutritionDay.getUser() != null && nutritionDay.getUser().getId() != null && nutritionDay.getUser().getId() != pathUserId ) 
            throw new PathAndModelDataMismatchException("Przesłany dziennik żywieniowy deklaruje przynależność do innego użytkownika, niż określa to ścieżka żądania");
    }
    
    public void validateCompatibilityOfPathAndModelData(NutritionDay nutritionDay, Long pathUserId, Long pathNutritionDayId){
        validateCompatibilityOfPathAndModelData(nutritionDay, pathUserId);
        if(nutritionDay.getId() != null && nutritionDay.getId() != pathNutritionDayId )
             throw new PathAndModelDataMismatchException("Przesłany dziennik żywieniowy deklaruje inny identyfikator, niż określa to ścieżka żądania");
    }
    
    public void validateNewEntityUniqueness(NutritionDay nutritionDay, Long userId){
        if(nutritionDayRepository.findByUserIdAndDate(userId, nutritionDay.getDate()) !=null)
            throw new EntityDuplicateException("Podany dziennik już istnieje");
    }
    
    public void validateUpdatedEntityUniqueness(NutritionDay nutritionDay, Long userId, Long nutritionDayId){
        NutritionDay currentNutritionDay = nutritionDayRepository.findOne(nutritionDayId);
        if(!currentNutritionDay.getDate().equals( nutritionDay.getDate())){
            validateNewEntityUniqueness(nutritionDay, userId);
        }
        
    }
    
    public void validateEntityExistance(Long pathUserId, Long pathNutritionDayId){
        if(nutritionDayRepository.findByUserIdAndId(pathUserId, pathNutritionDayId) == null)
            throw new EntityNotFoundException("Dziennik, który chcesz zaktualizować jeszcze nie istnieje");
    }
    
    public void validateCompabilityOfEntityRelationData(NutritionDay nutritionDay){
        if(nutritionDay.getMeals() != null){
            Collection<Meal> meals = nutritionDay.getMeals();
            meals.forEach((Meal meal) -> {
                if(meal.getNutritionDay() != null && meal.getNutritionDay().getId() != null && meal.getNutritionDay().getId() != nutritionDay.getId()){
                    throw new EntityRelationException("Treningi deklarują przynależność do innego dziennika treningowego");
                }
            });
        }
    }
    
    public void validateNutritionDayCreation(NutritionDay nutritionDay, Long pathUserId, Principal principal ){
        validateAuthorization(principal, pathUserId);
        validateCompatibilityOfPathAndModelData(nutritionDay, pathUserId);
        validateNewEntityUniqueness(nutritionDay,pathUserId);
  
    }
    
    public void validateNutritionDayUpdates(NutritionDay nutritionDay, Long pathUserId, Long pathNutritionDayId, Principal principal ){
        validateAuthorization(principal, pathUserId);
        validateEntityExistance(pathUserId, pathNutritionDayId);
        validateCompatibilityOfPathAndModelData(nutritionDay, pathUserId, pathNutritionDayId);
        validateUpdatedEntityUniqueness(nutritionDay, pathUserId, pathNutritionDayId);
  
    }
    
    public void validateNutritionDayDeletion(Long pathUserId, Long pathNutritionDayId, Principal principal ){
        validateAuthorization(principal, pathUserId);
        validateEntityExistance(pathUserId, pathNutritionDayId);
  
    }
}
