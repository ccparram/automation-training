package com.globant.automation.trainings.servicetesting.github.api.retrofit;

import com.globant.automation.trainings.servicetesting.github.models.Label;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

/**
 * @author Juan Krzemien
 */
public interface ReposApi {

    @POST("/repos/{userName}/{repoName}/issues/{id}/labels")
    Call<List<Label>> setLabelsByUserByRepoById(@Path("userName") String userName, @Path("repoName") String repoName, @Path("id") int id, @Body String something);

}
