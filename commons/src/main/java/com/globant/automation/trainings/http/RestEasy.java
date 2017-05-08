package com.globant.automation.trainings.http;

import com.globant.automation.trainings.logging.Logging;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import static com.globant.automation.trainings.config.CommonSettings.COMMON;

/**
 * Helper class to create HTTP clients based in JBoss's RestEasy library
 *
 * @author Juan Krzemien
 */
public class RestEasy implements Logging {

    private RestEasy() {
    }

    public static <T> T createApiFor(Class<T> api) {
        return createApiFor(new ResteasyClientBuilder(), api);
    }

    public static <T> T createApiFor(ResteasyClientBuilder builder, Class<T> api) {
        return createHttpClient(builder).target(COMMON.environment().getBaseUrl()).proxy(api);
    }

    /**
     * Creates an instance of an HTTP client based on RestEasyClient.
     *
     * @param builder
     * @return RestEasyClient instance
     */
    private static ResteasyClient createHttpClient(ResteasyClientBuilder builder) {
        if (COMMON.proxy().isEnabled()) {
            builder.defaultProxy(COMMON.proxy().getHost(), COMMON.proxy().getPort());
        }
        return builder.build();
    }

}

