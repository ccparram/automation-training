package com.globant.automation.trainings.servicetesting.standalone;

import com.globant.automation.trainings.servicetesting.config.Framework;
import com.globant.automation.trainings.servicetesting.logging.Logging;
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

    private final T validApi;
    private final T invalidApi;
    private final T restrictedApi;

    protected ServiceTestFor() {
        Class<T> serviceToTest = getApiType();
        this.validApi = getRetrofit(getValidOkHttpClient()).create(serviceToTest);
        this.invalidApi = getRetrofit(new OkHttpClient()).create(serviceToTest);
        this.restrictedApi = getRetrofit(getRestrictedOkHttpClient()).create(serviceToTest);
    }

    private OkHttpClient getValidOkHttpClient() {
        return new OkHttpClient.Builder()
                .authenticator(new BasicAuthenticator("juan.krzemien@globant.com", "R3m1lg4d0"))
                .build();
    }

    private OkHttpClient getRestrictedOkHttpClient() {
        return new OkHttpClient.Builder()
                .authenticator(new BasicAuthenticator("restricted_user", "restricted_password"))
                .build();
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

    protected T getValidApi() {
        return validApi;
    }

    protected T getInvalidApi() {
        return invalidApi;
    }

    protected T getRestrictedApi() {
        return restrictedApi;
    }

    protected <K> Response<K> call(Call<K> method) {
        try {
            return method.execute();
        } catch (IOException e) {
            getLogger().error("Could not perform service call!", e);
        }
        return null;
    }

}

