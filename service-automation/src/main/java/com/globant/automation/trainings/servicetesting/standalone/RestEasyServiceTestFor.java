package com.globant.automation.trainings.servicetesting.standalone;

import com.globant.automation.trainings.http.RestEasy;
import com.globant.automation.trainings.logging.Logging;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.ws.rs.client.ClientBuilder;
import java.lang.reflect.ParameterizedType;

/**
 * Simple base class for service tests classes to extend from.
 * <p>
 * Define the API to test (Retrofit's interface) as the generic type
 * for the class.
 *
 * @author Juan Krzemien
 */
public abstract class RestEasyServiceTestFor<T> implements Logging {

    private final T api;

    protected RestEasyServiceTestFor() {
        Class<T> serviceToTest = getApiType();
        ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
        configure(clientBuilder);
        this.api = RestEasy.createApiFor(clientBuilder, serviceToTest);
    }

    @SuppressWarnings("unchecked")
    private Class<T> getApiType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    protected void configure(ClientBuilder clientBuilder) {
    }

    protected T api() {
        return api;
    }

}

