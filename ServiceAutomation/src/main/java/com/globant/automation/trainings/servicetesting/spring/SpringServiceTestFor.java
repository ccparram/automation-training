package com.globant.automation.trainings.servicetesting.spring;

import com.globant.automation.trainings.servicetesting.logging.Logging;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * @author Juan Krzemien
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class SpringServiceTestFor<T> implements Logging {

    @Autowired
    private Retrofit client;

    @SuppressWarnings("unchecked")
    private Class<T> getApiType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
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
            return method.execute();
        } catch (IOException e) {
            getLogger().error("Could not perform service call!", e);
        }
        return null;
    }

    protected T api() {
        return client.create(getApiType());
    }

}

