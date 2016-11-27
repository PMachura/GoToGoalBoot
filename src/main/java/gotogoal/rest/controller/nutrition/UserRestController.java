/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller.nutrition;

import gotogoal.exception.EntityNotFoundException;
import gotogoal.model.user.User;
import gotogoal.rest.resource.user.UserResource;
import gotogoal.rest.resource.assembler.user.UserResourceAssembler;
import gotogoal.service.user.UserService;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Przemek
 */
@RestController
@ExposesResourceFor(User.class)
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    UserRestController(UserService userService) {
        this.userService = userService;

    }

    @RequestMapping("/principal")
    public Principal user(Principal user) {
        return user;
    }
    
    @RequestMapping("/logged")
    public User getLoggedUser(Authentication authentication){
        return userService.findByEmail(authentication.getName());
    }

    
    @RequestMapping("/{id}")
    public User findOne(@PathVariable Long id){
        return userService.findOne(id);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserResource> create(@RequestBody User user) {
        User created = userService.create(user);
        return new ResponseEntity(userService.mapToResource(created), HttpStatus.CREATED);
    }
}
