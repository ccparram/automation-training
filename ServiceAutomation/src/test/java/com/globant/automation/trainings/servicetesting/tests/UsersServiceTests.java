package com.globant.automation.trainings.servicetesting.tests;

import com.globant.automation.trainings.servicetesting.authenticators.BasicAuthenticator;
import com.globant.automation.trainings.servicetesting.models.Key;
import com.globant.automation.trainings.servicetesting.models.User;
import com.globant.automation.trainings.servicetesting.standalone.ServiceTestFor;
import com.globant.automation.trainings.servicetesting.tests.api.github.UsersApi;
import okhttp3.Authenticator;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Juan Krzemien
 */
public class UsersServiceTests extends ServiceTestFor<UsersApi> {

    @Override
    protected Authenticator setAuthenticator() {
        return new BasicAuthenticator("anEmail@somewhere.com", "aPassword");
    }

    @Test
    public void getSpecificUser() throws IOException {
        Response<User> response = call(api().getUsers("jkrzemien"));
        assertEquals("HTTP status is not OK", 200, response.code());
        User user = response.body();
        assertEquals("User name does not match", "Juan Krzemien", user.name);
    }

    @Test
    public void getUserPublicKey() throws IOException {
        Response<List<Key>> response = call(api().getUserPublicKeys("jdoe"));
        assertEquals("HTTP status is not OK", 200, response.code());
        List<Key> keys = response.body();
        keys.forEach(key -> {
            assertNotNull("Key holder should not be null", key);
            assertNotNull("Key ID should not be null", key.id);
            assertNotNull("Key should not be null", key.key);
        });
    }
}
