package com.globant.automation.trainings.servicetesting.standalone.authenticators;

import com.globant.automation.trainings.servicetesting.standalone.logging.Logging;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Juan Krzemien
 */
public abstract class AbstractAuthenticator implements okhttp3.Authenticator, Logging {

    private static final int MAX_RETRIES = 3;
    private final AtomicInteger retriesCounter = new AtomicInteger(0);

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        getLogger().info("Authenticating for response: " + response);
        getLogger().info("Challenges: " + response.challenges());

        if (retriesCounter.getAndIncrement() >= MAX_RETRIES) {
            retriesCounter.set(0);
            throw new IOException("Too many failed attempts to authenticate!");
        }

        return doAuthentication(route, response);
    }

    protected abstract Request doAuthentication(Route route, Response response);
}
