/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.jsonSerializer.user;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import gotogoal.model.user.User;
import gotogoal.rest.resource.user.UserResource;

/**
 *
 * @author Przemek
 */
public abstract class UserResourceMixIn extends UserResource {

    @Override
    @JsonUnwrapped
    public abstract User getUser();
}
