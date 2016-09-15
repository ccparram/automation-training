package com.globant.automation.trainings.servicetesting.tests;

import com.globant.automation.trainings.servicetesting.authenticators.BasicAuthenticator;
import com.globant.automation.trainings.servicetesting.models.User;
import com.globant.automation.trainings.servicetesting.standalone.ServiceTestFor;
import com.globant.automation.trainings.servicetesting.tests.api.github.UserApi;
import okhttp3.Authenticator;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Juan Krzemien
 */
public class UserServiceTests extends ServiceTestFor<UserApi> {

    @Override
    protected Authenticator setAuthenticator() {
        return new BasicAuthenticator("anEmail@somewhere.com", "aPassword");
    }

    @Test
    public void getAuthenticatedUser() throws IOException {
        Response<User> response = call(api().getAuthenticatedUser());
        assertEquals("HTTP status is not OK", 200, response.code());
        User user = response.body();
        assertEquals("User name does not match", "John Doe", user.name);
    }

}
