package com.globant.automation.trainings.servicetesting.standalone;

import com.globant.automation.trainings.servicetesting.config.Framework;
import com.globant.automation.trainings.servicetesting.logging.Logging;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.URL;

import static java.lang.String.format;

/**
 * Simple base class for service tests classes to extend from.
 *
 * Define the API to test (Retrofit's interface) as the generic type
 * for the class.
 *
 * @author Juan Krzemien
 */
public abstract class ServiceTestFor<T> implements Logging {

    private final T api;

    protected ServiceTestFor() {
        Class<T> serviceToTest = getApiType();
        this.api = getRetrofit().create(serviceToTest);
    }

    @SuppressWarnings("unchecked")
    private Class<T> getApiType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    protected abstract Authenticator setAuthenticator();

    /**
     * Creates an instance of an HTTP client based on OkHttpClient.
     *
     * @return OkHttpClient instance
     */
    protected OkHttpClient getOkHttpClient() {
        Authenticator authenticator = setAuthenticator();
        if (authenticator != null) {
            return new OkHttpClient.Builder()
                    .authenticator(authenticator)
                    .build();
        }
        return new OkHttpClient();
    }

    /**
     * Returns an instance ConverterFactory using Jackson for JSON marshalling
     *
     * @return Implementation of ConverterFactory using Jackson.
     */
    protected Converter.Factory getConverterFactory() {
        return JacksonConverterFactory.create();
    }

    /**
     * Given an HTTP client, create a Retrofit instance capable of invoking
     * the interface methods defined in this class generic type.
     * <p>
     * Assumes:
     * <p>
     * API send/receive JSON payloads. Override respective method to change that.
     * HTTP client to use is OkHttp 3. Override respective method to change that.
     *
     * @return new Retrofit instance
     */
    private Retrofit getRetrofit() {
        URL baseUrl = Framework.CONFIGURATION.getBaseUrl().orElseThrow(() -> new RuntimeException("Undefined base URL to test!"));
        return new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(baseUrl.toString())
                .addConverterFactory(getConverterFactory())
                .build();
    }

    /**
     * Helper method to actually execute Retrofit calls.
     * I don't like polluting tests with .execute() invocations or try/catch blocks.
     *
     * @param method Retrofit interface to call
     * @param <K>    Retrofit interface return type
     * @return Retrofit's Response of type K
     */
    protected <K> Response<K> call(Call<K> method) {
        try {
            getLogger().info(format("Invoking API: %s", method.request().toString()));
            return method.execute();
        } catch (IOException e) {
            getLogger().error(format("Could not perform service call for: %s", method.request().toString()), e);
        }
        return null;
    }

    protected T api() {
        return api;
    }

}

