package com.globant.automation.trainings.servicetesting.api.github;

import com.globant.automation.trainings.servicetesting.models.Key;
import com.globant.automation.trainings.servicetesting.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

/**
 * @author Juan Krzemien
 */
public interface UsersApi {

    @GET("/users/{userName}")
    Call<User> getUsers(@Path("userName") String userName);

    @GET("/users/{userName}/keys")
    Call<List<Key>> getUserPublicKeys(@Path("userName") String userName);

}
