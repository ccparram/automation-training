package com.globant.automation.trainings.servicetesting.tests.api;

import com.globant.automation.trainings.servicetesting.tests.models.TokenResponse;
import retrofit2.http.GET;

/**
 * Created by Juan Krzemien on 9/13/2016.
 */
public interface Authorization {

    @GET("/login")
    TokenResponse retrieveTokenFor(String username, String password);

}
