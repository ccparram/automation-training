package com.globant.automation.trainings.servicetesting;

import com.globant.automation.trainings.servicetesting.config.Framework;
import retrofit2.Retrofit;

import java.lang.reflect.ParameterizedType;
import java.net.URL;

/**
 * @author Juan Krzemien
 */
public abstract class ServiceTestForTokenSecured<A, T> extends ServiceTestFor<T> {

    private final A authorizationApi;

    protected ServiceTestForTokenSecured() {
        URL baseUrl = Framework.CONFIGURATION.getBaseUrl().orElseThrow(() -> new RuntimeException("Undefined base URL to test!"));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl.toString())
                .build();
        this.authorizationApi = retrofit.create(getAuthorizationApiType());
    }

    @SuppressWarnings("unchecked")
    private Class<A> getAuthorizationApiType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<A>) parameterizedType.getActualTypeArguments()[0];
    }

    protected A getAuthorizationApi() {
        return authorizationApi;
    }

    protected abstract Credentials getCredentials();
}

