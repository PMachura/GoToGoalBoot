/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model.user;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Przemek
 */

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession implements Serializable{

    private Long id;
    private String firstName;  
    private String lastName;
    private LocalDate birthDate;
    private URL picturePath;
    
    public void saveUser(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.birthDate = user.getBirthDate();
    }
    
    public User toUser(){
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthDate(birthDate);
        return user;
    }
    
    public void setPicturePath(Resource picturePath) throws IOException{
        this.picturePath = picturePath.getURL();
    }
    
    public Resource getPicturePath(){
        return picturePath == null ? null : new UrlResource(picturePath);
    }
}
