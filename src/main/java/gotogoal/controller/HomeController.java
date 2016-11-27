package gotogoal.controller;

import gotogoal.model.user.User;
import gotogoal.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    
        UserService userService;
        
        public HomeController(UserService userService){
            this.userService = userService;
        }
        @RequestMapping("/home")
        public String rest(){
            return "layout/default";
        }
}
