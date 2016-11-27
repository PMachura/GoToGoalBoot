/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.user;

import gotogoal.exception.EntityNotFoundException;
import gotogoal.model.user.User;
import gotogoal.rest.controller.nutrition.UserRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.ResourceSupport;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 *
 * @author Przemek
 */
public class UserResource extends ResourceSupport {
       
    private  User user;
    
    public UserResource(){
        
    }
    
    public UserResource(User user) {
        this.user = user; 
        
        //w metodzie link to mozemy podawac wartosci po przecinku są to: Kontroler, Argumenty mapowania klasy kontrolerea tz
        //w methodOn działa podobnie - wtedy odnosimy się do argumentów @RequestMapping metody np Kotroler {userId} 
 //      this.add(linkTo(UserRestController.class).slash(user.getId()).withRel("selfByController"));
//        this.add(linkTo(methodOn(UserRestController.class).show(user.getId())).withRel("selfByControllerMethod"));
           
    }
    
    
    public User getUser(){
        return user;
    }
}
