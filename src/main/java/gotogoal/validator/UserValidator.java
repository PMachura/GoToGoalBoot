/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.validator;

import gotogoal.exception.EntityDuplicateException;
import gotogoal.exception.EntityNotFoundException;
import gotogoal.exception.ForbiddenResourcesException;
import gotogoal.exception.PathAndModelDataMismatchException;
import gotogoal.model.user.User;
import gotogoal.repository.user.UserRepository;
import java.security.Principal;
import org.springframework.stereotype.Component;

/**
 *
 * @author Przemek
 */
@Component
public class UserValidator {
    
    private UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    
    public void validateAuthorization(Principal principal, Long pathUserId){    
        if(principal == null || userRepository.findByEmail(principal.getName()).getId() != pathUserId) throw new ForbiddenResourcesException("Próbujesz dostać się do zasobów innego użytkownika");
    }
    
    public void validateCompatibilityOfPathAndModelData(User user, Long pathUserId){
        if(user.getId() != null && user.getId() != pathUserId ) 
            throw new PathAndModelDataMismatchException("Przesłany dziennik żywieniowy deklaruje przynależność do innego użytkownika, niż określa to ścieżka żądania");
    }
    
   
    public void validateEntityExistance(Long pathUserId){
        if(userRepository.findOne(pathUserId) == null)
            throw new EntityNotFoundException("Dziennik, który chcesz zaktualizować jeszcze nie istnieje");
    }
    
    public void validateNewEntityUniqueness(User user){
        if(userRepository.findByEmail(user.getEmail()) != null) throw new EntityDuplicateException("Podany email jest już zajęty");
    }
    
    public void validateUpdatedEntityUniqueness(User user, Long userId){
        User currentUser = userRepository.findOne(userId);
        if(!currentUser.getEmail().equals(user.getEmail())){
            System.out.println("EMail1" + user.getEmail() + " 2 " + currentUser.getEmail());
            validateNewEntityUniqueness(user);
        }
    }
    
    public void validateUserCreation(User user){
        validateNewEntityUniqueness(user);
    }
    
    public void validateUserUpdates(User user, Long pathUserId, Principal principal ){
        validateAuthorization(principal, pathUserId);
        validateEntityExistance(pathUserId);
        validateCompatibilityOfPathAndModelData(user, pathUserId);
        validateUpdatedEntityUniqueness(user, pathUserId);
  
    }
    
    public void validateUserDeletion(Long pathUserId, Principal principal ){
        validateAuthorization(principal, pathUserId);
        validateEntityExistance(pathUserId);
  
    }
    
}
