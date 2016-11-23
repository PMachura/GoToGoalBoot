/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.service;

import gotogoal.exception.EntityNotFoundException;
import gotogoal.model.User;
import gotogoal.repository.UserRepository;
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

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findOneWithException(Long id) throws EntityNotFoundException {
        if (!userRepository.exists(id)) {
            throw new EntityNotFoundException("User with id " + id + " does not exists");
        }
        return userRepository.findOne(id);
    }
    
    public User findOne(Long id){
        return userRepository.findOne(id);
    }

    public User findByEmail(String userEmail){
        return userRepository.findByEmail(userEmail);
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User updateLastName(User user, String lastName) {
        user.setLastName(lastName);
        return userRepository.save(user);
    }

    public User updateEmail(User user, String email) throws EntityNotFoundException {
        if (!userRepository.exists(user.getId())) {
            throw new EntityNotFoundException("User with id " + user.getId() + " does not exists");
        }
        user.setEmail(email);
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email) == null ? false : true;
    }

    public void deleteByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    public void delete(Long id) throws EntityNotFoundException {
        if (!userRepository.exists(id)) {
            throw new EntityNotFoundException("User with id " + id + " does not exists");
        }
        userRepository.delete(id);
    }

}
