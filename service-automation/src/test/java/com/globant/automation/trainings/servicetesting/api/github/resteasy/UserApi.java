package com.globant.automation.trainings.servicetesting.api.github.resteasy;

import com.globant.automation.trainings.servicetesting.models.User;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;

public interface UserApi {

    @GET
    @Path("/user")
    User getAuthenticatedUser(@HeaderParam("someHeader") String value);

}
