package com.globant.automation.trainings.servicetesting.authenticators;

import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * @author Juan Krzemien
 */
public class BasicAuthenticator extends AbstractAuthenticator {

    private final String username;
    private final String password;

    public BasicAuthenticator(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    @Override
    protected Request doAuthentication(Route route, Response response) {
        return response.request().newBuilder()
                .header("Authorization", Credentials.basic(username, password))
                .build();
    }
}
