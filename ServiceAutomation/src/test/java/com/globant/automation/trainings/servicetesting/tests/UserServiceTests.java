package com.globant.automation.trainings.servicetesting.tests;

import com.globant.automation.trainings.servicetesting.models.User;
import com.globant.automation.trainings.servicetesting.standalone.ServiceTestFor;
import com.globant.automation.trainings.servicetesting.tests.api.github.UserApi;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Juan Krzemien
 */
public class UserServiceTests extends ServiceTestFor<UserApi> {

    @Test
    public void getAuthenticatedUser() throws IOException {
        Response<User> response = call(getValidApi().getAuthenticatedUser());
        assertEquals("HTTP status is not OK", 200, response.code());
        User user = response.body();
        assertEquals("User name does not match", "Juan Krzemien", user.name);
    }

    @Test
    public void getUserPublicKey() throws IOException {
        Response<User> response = call(getValidApi().getUserPublicKey("jkrzemien"));
        assertEquals("HTTP status is not OK", 200, response.code());
        User user = response.body();
        assertEquals("User name does not match", "Juan Krzemien", user.name);
    }

}
