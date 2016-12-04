/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.resource.assembler.user;

import gotogoal.exception.EntityNotFoundException;
import gotogoal.model.user.User;
import gotogoal.rest.controller.nutrition.NutritionDayRestController;
import gotogoal.rest.controller.user.UserRestController;
import gotogoal.rest.controller.workout.WorkoutDayRestController;
import gotogoal.rest.resource.user.UserResource;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 *
 * @author Przemek
 */
@Component
public class UserResourceAssembler extends ResourceAssemblerSupport<User, UserResource> {

    public UserResourceAssembler() {
        super(UserRestController.class, UserResource.class);
    }

    @Override
    public UserResource toResource(User user) {
        UserResource userResource = createResourceWithId(user.getId(), user);
        userResource.add(linkTo(NutritionDayRestController.class, user.getId()).withRel("nutritionDays"));
        userResource.add(linkTo(WorkoutDayRestController.class, user.getId()).withRel("workoutDays"));
        return userResource;
    }

    @Override
    protected UserResource instantiateResource(User user) {

        UserResource userResource = new UserResource(user);
        return userResource;

    }

}
