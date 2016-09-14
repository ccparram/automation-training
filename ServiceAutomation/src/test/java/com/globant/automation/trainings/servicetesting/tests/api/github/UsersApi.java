package com.globant.automation.trainings.servicetesting.tests.api.github;

import com.globant.automation.trainings.servicetesting.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Juan Krzemien
 */
public interface UsersApi {

    @GET("/users/{userName}")
    Call<User> getUsers(@Path("userName") String userName);

}
