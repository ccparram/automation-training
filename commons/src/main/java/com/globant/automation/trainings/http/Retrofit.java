package com.globant.automation.trainings.http;

import com.globant.automation.trainings.logging.Logging;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.globant.automation.trainings.config.CommonSettings.COMMON;

/**
 * Helper class to create HTTP clients based in JBoss's RestEasy library
 *
 * @author Juan Krzemien
 */
public class Retrofit implements Logging {

    private Retrofit() {
    }

    /**
     * Create a Retrofit instance capable of invoking the interface methods
     * defined in this class generic type.
     * <p>
     * Assumes:
     * <p>
     * API send/receive JSON payloads
     * HTTP client to use is OkHttp 3
     *
     * @return new API instance proxied by Retrofit
     */
    public static <T> T createApiFor(Class<T> api) {
        return createApiFor(new OkHttpClient.Builder(), api);
    }

    public static <T> T createApiFor(OkHttpClient.Builder clientBuilder, Class<T> api) {
        return new retrofit2.Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(COMMON.environment().getBaseUrl())
                .addConverterFactory(getConverterFactory())
                .build()
                .create(api);
    }

    /**
     * Returns an instance ConverterFactory using Jackson for JSON marshalling
     *
     * @return Implementation of ConverterFactory using Jackson.
     */
    private static Converter.Factory getConverterFactory() {
        return JacksonConverterFactory.create();
    }

}

