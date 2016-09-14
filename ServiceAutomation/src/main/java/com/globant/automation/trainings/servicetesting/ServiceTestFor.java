package com.globant.automation.trainings.servicetesting;

import com.globant.automation.trainings.servicetesting.config.Framework;
import retrofit2.Retrofit;

import java.lang.reflect.ParameterizedType;
import java.net.URL;

/**
 * @author Juan Krzemien
 */
class ServiceTestFor<T> {

    private final T validApi;
    private final T invalidApi;
    private final T restrictedApi;

    protected ServiceTestFor() {
        URL baseUrl = Framework.CONFIGURATION.getBaseUrl().orElseThrow(() -> new RuntimeException("Undefined base URL to test!"));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl.toString())
                .build();
        this.validApi = retrofit.create(getPageObjectType());
        this.invalidApi = retrofit.create(getPageObjectType());
        this.restrictedApi = retrofit.create(getPageObjectType());
    }

    @SuppressWarnings("unchecked")
    private Class<T> getPageObjectType() {
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
}

