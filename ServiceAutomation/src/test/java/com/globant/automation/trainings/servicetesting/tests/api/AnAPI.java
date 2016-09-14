package com.globant.automation.trainings.servicetesting.tests.api;

import com.globant.automation.trainings.servicetesting.tests.models.MyPojo;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Juan Krzemien
 */

public interface AnAPI {

    @POST("/interesting")
    Call<Response> createInterestingStuff(@Body MyPojo stuff);

    @GET("/interesting/{aParam}")
    Call<MyPojo> getInterestingStuff(@Path("aParam") String aParameter);
}
