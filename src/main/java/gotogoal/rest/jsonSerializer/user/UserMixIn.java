/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.jsonSerializer.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import gotogoal.model.nutrition.NutritionDay;
import gotogoal.model.user.User;
import java.util.List;

/**
 *
 * @author Przemek
 */
public abstract class UserMixIn extends User{
    
    @JsonIgnore
    @Override
    public abstract String getPassword();

    @JsonProperty
    @Override
    public abstract void setPassword(String password);
    
    @JsonIgnore
    @Override
    public abstract List<NutritionDay> getNutritionDay();
    
}
