package com.globant.automation.trainings.servicetesting.authenticators;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * @author Juan Krzemien
 */
public class TokenAuthentication extends AbstractAuthenticator {

    private final String token;

    public TokenAuthentication(String access_token) {
        this.token = access_token;
    }

    @Override
    protected Request doAuthentication(Route route, Response response) {
        return response.request().newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();
    }
}
