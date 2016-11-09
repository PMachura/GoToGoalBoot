package gotogoal.controller;

import gotogoal.model.User;
import gotogoal.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    
        UserService userService;
        
        public HomeController(UserService userService){
            this.userService = userService;
        }
        @RequestMapping("/angularRest")
        public String rest(){
            return "angularRest/angularRest";
        }
    
	@RequestMapping("/")
	public String home(){
		return "home";
	}
        
        @RequestMapping("/firstAngularApp")
        public String firstAngularApp(){
            return "/firstAngularApp";
        }
        
        @RequestMapping("/angularSecurity")
        public String angularSecurity(){
            return "/angularSecurity/index";
        }
        
        @RequestMapping("/securityTest")
        public String securityTest(){
            return "home";
        }
        
        @RequestMapping("/test/{email}")
        public String test(@PathVariable String email){
  
         
            userService.deleteByEmail(email);
            return "home";
        }
        
	
}
