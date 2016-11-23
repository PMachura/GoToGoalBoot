/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.assembler;

import gotogoal.model.NutritionDay;
import gotogoal.rest.controller.MealRestController;
import gotogoal.rest.controller.NutritionDayRestController;
import gotogoal.rest.resource.NutritionDayResource;
import java.time.LocalDate;
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
//class NutritionDayPageAssembler {
//
//    public NutritionDayResource toResource(LocalDate localDate, Page<NutritionDay> page) {
//        System.out.println("@@@@@ETAP 1");
//        NutritionDayResource nutritionDayResource = new NutritionDayResource(page.getContent());
//        System.out.println("@@@@@ETAP 2");
//        if (page.hasNext()) {
//            nutritionDayResource.add(linkTo(methodOn(NutritionDayRestController.class)
//                    .findAll(localDate, page.getNumber() + 1, page.getSize())).withRel("nextPage"));
//        }
//        System.out.println("@@@@@ETAP 3");
//        if (page.hasPrevious()) {
//            nutritionDayResource.add(linkTo(methodOn(NutritionDayRestController.class)
//                    .findAll(localDate, page.getNumber() - 1, page.getSize())).withRel("nextPage"));
//        }
//        System.out.println("@@@@@ETAP 4");
//        nutritionDayResource.add(linkTo(methodOn(NutritionDayRestController.class)
//                .findAll(localDate, page.getNumber() - 1, page.getSize())).withSelfRel());
//        System.out.println("@@@@@ETAP 5");
//        return nutritionDayResource;
//    }
//}

@Component
public class NutritionDayResourceAssembler extends ResourceAssemblerSupport<NutritionDay, NutritionDayResource> {

   // private NutritionDayPageAssembler nutritionDayPageAssembler;

    public NutritionDayResourceAssembler() {
        super(NutritionDayRestController.class, NutritionDayResource.class);
      //  this.nutritionDayPageAssembler = new NutritionDayPageAssembler();
    }

    @Override
    public NutritionDayResource toResource(NutritionDay nutritionDay) {
        NutritionDayResource nutritionDayResource = new NutritionDayResource(nutritionDay);
        nutritionDayResource.add(linkTo(NutritionDayRestController.class,nutritionDay.getUser().getId()).slash(nutritionDay.getId()).withSelfRel());
        return nutritionDayResource;
    }

    public NutritionDayResource toResource(LocalDate localDate, Page<NutritionDay> page) {
        NutritionDayResource nutritionDayResource = new NutritionDayResource(page.getContent());
//        if (page.hasNext()) {
//            nutritionDayResource.add(linkTo(methodOn(NutritionDayRestController.class)
//                    .findAll(localDate, page.getNumber() + 1, page.getSize())).withRel("nextPage"));
//        }
//        if (page.hasPrevious()) {
//            nutritionDayResource.add(linkTo(methodOn(NutritionDayRestController.class)
//                    .findAll(localDate, page.getNumber() - 1, page.getSize())).withRel("previousPage"));
//        }
//        nutritionDayResource.add(linkTo(methodOn(NutritionDayRestController.class)
//                .findAll(localDate, page.getNumber(), page.getSize())).withSelfRel());

        return nutritionDayResource;
    }

    @Override
    protected NutritionDayResource instantiateResource(NutritionDay nutritionDay) {
        NutritionDayResource nutritionDayResource = new NutritionDayResource(nutritionDay);
        return nutritionDayResource;
    }
}
