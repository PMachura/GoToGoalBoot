/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller;

import gotogoal.exception.EntityNotFoundException;
import gotogoal.model.User;
import gotogoal.rest.resource.UserResource;
import gotogoal.rest.resource.assembler.UserResourceAssembler;
import gotogoal.service.UserService;
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

    @RequestMapping("/{id}")
    public String getUser() {
        return "/home";
    }

    // wtym podejściu wysyłamy po prostu klasę UserResource - wszystkie pola klasy są przesyłane i w odpowiedzi widziane w formacie json-
    // linki które zawiera klasa są traktowane tak samo jak np Imie i nazwisko usera
    // w odpowiedzi xml jest <UserResource> ...(dane Usera) ... </UserResource>
    @RequestMapping(value = "/userResource/{userId}", method = RequestMethod.GET)
    public UserResource show(@PathVariable Long userId) throws EntityNotFoundException {
        return new UserResource(userService.findOneWithException(userId));
    }
    
    @RequestMapping(value = "/userResourceFromAssembler/{userId}", method = RequestMethod.GET)
    public UserResource show3(@PathVariable Long userId) throws EntityNotFoundException {
        UserResource userResource = new UserResourceAssembler().toResource(userService.findOneWithException(userId));
        return userResource;
    }

    // w tym podejściu ResponseEntity dodaje linki z klasy UserResource do nagłówka odpowiedzi http, a ciało to informacje o użytkoniku - tzn. np. jego imie nazwisko itd.
    @RequestMapping(value = "/userResponseEntity/{userId}", method = RequestMethod.GET)
    public ResponseEntity<User> show2(@PathVariable Long userId) throws EntityNotFoundException {

        HttpHeaders httpHeaders = new HttpHeaders();
        UserResource userResource = new UserResource(userService.findOneWithException(userId));
        Link linkUser = userResource.getLink("selfByController");
        Link linkMethod = userResource.getLink("selfByControllerMethod");
        httpHeaders.setLocation(URI.create(linkUser.getHref()));
        httpHeaders.add("testowy", linkMethod.getHref());
        return new ResponseEntity<User>(userResource.getUser(), httpHeaders, HttpStatus.ACCEPTED);
    }
    
    @RequestMapping(value = "/userResourceResponseEntity/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserResource> show4(@PathVariable Long userId) throws EntityNotFoundException {

        HttpHeaders httpHeaders = new HttpHeaders();
        UserResource userResource = new UserResource(userService.findOneWithException(userId));
        Link linkUser = userResource.getLink("selfByController");
        Link linkMethod = userResource.getLink("selfByControllerMethod");
        httpHeaders.setLocation(URI.create(linkUser.getHref()));
        httpHeaders.add("testowy", linkMethod.getHref());
        return new ResponseEntity<UserResource>(userResource, httpHeaders, HttpStatus.ACCEPTED);
    }

    

//    @RequestMapping(value = "/4", method = RequestMethod.GET)
//    public Resources<UserResource> show4() throws EntityNotFoundException {
//    
//        List<UserResource> userResourceList = userService.findAll().stream().map(UserResource::new).collect(Collectors.toList());
//        
//    }
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<User> findAll() {
        List<User> users = new ArrayList<User>(userService.findAll());
        return users;
    }

    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public Map<String, List<User>> findAllMap() {
        Map<String, List<User>> users = new HashMap<String, List<User>>();
        users.put("users", userService.findAll());
        return users;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<User> create(@RequestBody User user) throws EntityNotFoundException {
        User saved = userService.save(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        Link linkUser = new UserResource(saved).getLink("users");
        Link linkMethod = new UserResource(saved).getLink("self");
        httpHeaders.setLocation(URI.create(linkUser.getHref()));
        httpHeaders.add("testowy", linkMethod.getHref());

        return new ResponseEntity<>(saved, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        HttpStatus status = HttpStatus.OK;
        if (!userService.existsByEmail(user.getEmail())) {
            status = HttpStatus.CREATED;
        }
        User saved = userService.save(user);
        return new ResponseEntity<>(saved, status);
    }

    @RequestMapping(value = "/{email}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateEmail(@PathVariable String email, @RequestBody User user) throws EntityNotFoundException {
        User saved = userService.updateEmail(user, email);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{email}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable String email) throws EntityNotFoundException {
        userService.deleteByEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
