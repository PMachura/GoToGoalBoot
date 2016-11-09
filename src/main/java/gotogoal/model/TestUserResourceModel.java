/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author Przemek
 */
@Entity
public class TestUserResourceModel extends ResourceSupport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ide;
    
    String name;
    
    String email;
    
    String password;

    public Long getIde() {
        return ide;
    }

    public void setIde(Long id) {
        this.ide = ide;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
    
}
