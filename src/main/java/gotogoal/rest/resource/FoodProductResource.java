/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import gotogoal.model.FoodProduct;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author Przemek
 */
public class FoodProductResource extends ResourceSupport {
    

    private FoodProduct foodProduct;
    
    public FoodProductResource(){}
    public FoodProductResource(FoodProduct foodProduct){
        this.foodProduct = foodProduct;
    }
    
    
    public FoodProduct getFoodProduct(){
        return this.foodProduct;
    }
    
  
}
