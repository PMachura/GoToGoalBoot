/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.assembler;

import gotogoal.model.FoodProduct;
import gotogoal.rest.controller.FoodProductRestController;
import gotogoal.rest.resource.FoodProductResource;
import gotogoal.rest.resource.UserResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 *
 * @author Przemek
 */
@Component
public class FoodProductAssembler extends ResourceAssemblerSupport<FoodProduct, FoodProductResource> {

    public FoodProductAssembler() {
        super(FoodProductRestController.class, FoodProductResource.class);
    }

    @Override
    public FoodProductResource toResource(FoodProduct entity) {
        FoodProductResource foodProductResource = createResourceWithId(entity.getId(), entity);
        return foodProductResource;
    }
    
    protected FoodProductResource instantiateResource(FoodProduct entity){
        FoodProductResource foodProductResource = new FoodProductResource(entity);
        return foodProductResource;
    }
    
}
