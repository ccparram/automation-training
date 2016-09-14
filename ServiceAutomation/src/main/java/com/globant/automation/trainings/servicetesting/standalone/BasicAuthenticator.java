package com.globant.automation.trainings.servicetesting.standalone;

import com.globant.automation.trainings.servicetesting.logging.Logging;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import java.io.IOException;

/**
 * @author Juan Krzemien
 */
public class BasicAuthenticator implements okhttp3.Authenticator, Logging {

    private final String username;
    private final String password;

    public BasicAuthenticator(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        getLogger().info("Authenticating for response: " + response);
        getLogger().info("Challenges: " + response.challenges());

        String credential = Credentials.basic(username, password);

        // Or create/refresh your access_token using a synchronous api request to your API
        // newAccessToken = service.refreshToken();

        // Add new header to rejected request and retry it
        return response.request().newBuilder()
                .header("Authorization", credential)
                .build();
    }
}
