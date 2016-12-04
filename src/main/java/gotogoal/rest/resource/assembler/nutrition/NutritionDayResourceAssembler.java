/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.assembler.nutrition;

import gotogoal.model.nutrition.NutritionDay;
import gotogoal.rest.controller.nutrition.MealRestController;
import gotogoal.rest.controller.nutrition.NutritionDayRestController;
import gotogoal.rest.controller.user.UserRestController;
import gotogoal.rest.resource.nutrition.NutritionDayResource;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 *
 * @author Przemek
 */

@Component
public class NutritionDayResourceAssembler extends ResourceAssemblerSupport<NutritionDay, NutritionDayResource> {

   // private NutritionDayPageAssembler nutritionDayPageAssembler;

    public NutritionDayResourceAssembler() {
        super(NutritionDayRestController.class, NutritionDayResource.class);
     
    }

    @Override
    public NutritionDayResource toResource(NutritionDay nutritionDay) {
        NutritionDayResource nutritionDayResource = new NutritionDayResource(nutritionDay);
        nutritionDayResource.add(linkTo(NutritionDayRestController.class,nutritionDay.getUser().getId()).slash(nutritionDay.getId()).withSelfRel());
        nutritionDayResource.add(linkTo(UserRestController.class).slash(nutritionDay.getUser().getId()).withRel("user"));
        nutritionDayResource.add(linkTo(MealRestController.class,nutritionDay.getUser().getId(),nutritionDay.getId()).withRel("meals"));
        return nutritionDayResource;
    }
    
    private Collection<NutritionDayResource> toResource(Collection<NutritionDay> collection) {
        return Arrays.asList(collection.stream().map(this::toResource)
                .toArray(NutritionDayResource[]::new));
    }
    
    public Page<NutritionDayResource> toResource(Page<NutritionDay> nutritionDayPage) {
        return nutritionDayPage.map(this::toResource);
    }

    public NutritionDayResource toResource(LocalDate localDate, Page<NutritionDay> page) {
        NutritionDayResource nutritionDayResource = new NutritionDayResource(page.getContent());

        return nutritionDayResource;
    }

    @Override
    protected NutritionDayResource instantiateResource(NutritionDay nutritionDay) {
        NutritionDayResource nutritionDayResource = new NutritionDayResource(nutritionDay);
        return nutritionDayResource;
    }
}
