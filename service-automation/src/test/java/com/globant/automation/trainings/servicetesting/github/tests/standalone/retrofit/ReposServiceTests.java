package com.globant.automation.trainings.servicetesting.github.tests.standalone.retrofit;

import com.globant.automation.trainings.servicetesting.authenticators.TokenAuthentication;
import com.globant.automation.trainings.servicetesting.github.api.retrofit.ReposApi;
import com.globant.automation.trainings.servicetesting.github.models.Label;
import com.globant.automation.trainings.servicetesting.standalone.RetrofitServiceTestFor;
import okhttp3.OkHttpClient;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Juan Krzemien
 */
public class ReposServiceTests extends RetrofitServiceTestFor<ReposApi> {

    @Override
    protected void configure(OkHttpClient.Builder client) {
        client.authenticator(new TokenAuthentication("lets_assume_this_is_a_token_ok?"));
    }

    @Test
    public void attemptToCreateLabels() throws IOException {
        Response<List<Label>> response = call(api().setLabelsByUserByRepoById("aUser", "aRepo", 1, "Damn!"));
        assertEquals("HTTP status is not OK", 200, response.code());
        List<Label> labels = response.body();
        labels.forEach(label -> {
            assertNotNull("Label name should not be null", label.name);
            assertNotNull("Label color should not be null", label.color);
            assertNotNull("Label url should not be null", label.url);
        });
    }

}
