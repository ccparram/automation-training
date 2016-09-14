package com.globant.automation.trainings.servicetesting.tests;

import com.globant.automation.trainings.servicetesting.models.User;
import com.globant.automation.trainings.servicetesting.standalone.ServiceTestFor;
import com.globant.automation.trainings.servicetesting.tests.api.github.UsersApi;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Juan Krzemien
 */
public class UsersServiceTests extends ServiceTestFor<UsersApi> {

    @Test
    public void getSpecificUser() throws IOException {
        Response<User> response = call(getValidApi().getUsers("jkrzemien"));
        assertEquals("HTTP status is not OK", 200, response.code());
        User user = response.body();
        assertEquals("User name does not match", "Juan Krzemien", user.name);
    }

}
