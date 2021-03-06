/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import gotogoal.config.LocalDateAttributeConverter;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import gotogoal.config.PastLocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.OneToMany;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author Przemek
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 20)
    private String lastName;
    
    @NotNull
    @Size(min = 2, max = 20)
    private String password;

    @PastLocalDate
    @NotNull
    @Convert(converter = LocalDateAttributeConverter.class)
    //@JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Email
    @Column(unique = true)
    private String email;
    
    @OneToMany(mappedBy="user")
    private List<NutritionDay> nutritionDay;
    

    public User() {

    }

    public User(Long id, String firstName, String lastName, LocalDate birthDate, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
    }

    public User(String firstName, String lastName, LocalDate birthDate, String email) {
        this(null, firstName, lastName, birthDate, email);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public List<NutritionDay> getDailyNutrition() {
        return nutritionDay;
    }

    public void setDailyNutrition(List<NutritionDay> nutritionDay) {
        this.nutritionDay = nutritionDay;
    }
    
}
