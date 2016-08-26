/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model;


import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import gotogoal.config.PastLocalDate;



/**
 *
 * @author Przemek
 */

@Entity
public class User {
    
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Size(min=2, max=20)
    private String firstName;
    
    @NotNull
    @Size(min=2,max=20)
    private String lastName;
    
   // @PastLocalDate
    @NotNull
    private LocalDate birthDate;
    
    
    public User(){
    	
    }
    
    public User(Long id, String firstName, String lastName, LocalDate birthDate){
    	this.id =id;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.birthDate = birthDate;
    }

    public User(String firstName, String lastName, LocalDate birthDate){
    	this(null,firstName,lastName,birthDate);
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

    
    
    
}
