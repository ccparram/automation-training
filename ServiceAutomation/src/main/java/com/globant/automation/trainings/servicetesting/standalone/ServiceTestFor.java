package com.globant.automation.trainings.servicetesting.standalone;

import com.globant.automation.trainings.servicetesting.config.Framework;
import com.globant.automation.trainings.servicetesting.logging.Logging;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.URL;

/**
 * @author Juan Krzemien
 */
public abstract class ServiceTestFor<T> implements Logging {

    private final T api;

    protected ServiceTestFor() {
        Class<T> serviceToTest = getApiType();
        this.api = getRetrofit(getValidOkHttpClient()).create(serviceToTest);
    }

    protected abstract Authenticator setAuthenticator();

    private OkHttpClient getValidOkHttpClient() {
        Authenticator authenticator = setAuthenticator();
        if (authenticator != null) {
            return new OkHttpClient.Builder()
                    .authenticator(authenticator)
                    .build();
        }
        return new OkHttpClient();
    }

    protected Retrofit getRetrofit(OkHttpClient client) {
        URL baseUrl = Framework.CONFIGURATION.getBaseUrl().orElseThrow(() -> new RuntimeException("Undefined base URL to test!"));
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl.toString())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @SuppressWarnings("unchecked")
    private Class<T> getApiType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    protected <K> Response<K> call(Call<K> method) {
        try {
            return method.execute();
        } catch (IOException e) {
            getLogger().error("Could not perform service call!", e);
        }
        return null;
    }

    protected T api() {
        return api;
    }

}

