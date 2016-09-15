package com.globant.automation.trainings.servicetesting.standalone.tests.api.github;

import com.globant.automation.trainings.servicetesting.standalone.models.User;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Juan Krzemien
 */
public interface UserApi {

    @GET("/user")
    Call<User> getAuthenticatedUser();

}
