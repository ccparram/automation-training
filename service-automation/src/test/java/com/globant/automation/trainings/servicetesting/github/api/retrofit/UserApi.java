package com.globant.automation.trainings.servicetesting.github.api.retrofit;

import com.globant.automation.trainings.servicetesting.github.models.User;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Juan Krzemien
 */
public interface UserApi {

    @GET("/user")
    Call<User> getAuthenticatedUser();

}
