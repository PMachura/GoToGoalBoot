/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service.user;

import gotogoal.exception.EntityNotFoundException;
import gotogoal.model.user.User;
import gotogoal.repository.user.UserRepository;
import gotogoal.rest.resource.assembler.user.UserResourceAssembler;
import gotogoal.rest.resource.user.UserResource;
import gotogoal.validator.UserValidator;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Przemek
 */
@Service
public class UserService {

    final UserRepository userRepository;
    final UserResourceAssembler userResourceAssembler;
    private final UserValidator userValidator;

    @Autowired
    public UserService(UserRepository userRepository, UserResourceAssembler userResourceAssembler, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userResourceAssembler = userResourceAssembler;
        this.userValidator = userValidator;
    }
 
    
   public User findOne(Long userId){
       return userRepository.findOne(userId);
   }
   
   public User create(User user){
       userValidator.validateUserCreation(user);
       return userRepository.save(user);
   }
   
   public User findByEmail(String email){
       return userRepository.findByEmail(email);
   }

    public User update(User user, Long id,  Principal principal) {
        userValidator.validateUserUpdates(user, id, principal);
        return userRepository.save(user);
    }

}
