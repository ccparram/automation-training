package com.globant.automation.trainings.servicetesting.tests.api.github;

import com.globant.automation.trainings.servicetesting.models.Authorization;
import com.globant.automation.trainings.servicetesting.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Juan Krzemien
 */
public interface Authorizations {

    @POST("/authorizations")
    Call<User> create(@Body Authorization authorization);

}
