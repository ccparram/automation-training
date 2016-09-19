package com.globant.automation.trainings.servicetesting.tests.spring;

import com.globant.automation.trainings.servicetesting.authenticators.TokenAuthentication;
import com.globant.automation.trainings.servicetesting.spring.AbstractTestContext;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author Juan Krzemien
 */
public class TestContext extends AbstractTestContext {

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${token}")
    private String token;

    @Bean
    Authenticator getAuthenticator() {
        return new TokenAuthentication(token);
    }

    @Bean
    OkHttpClient getOkHttpClient(Authenticator authenticator) {
        if (authenticator != null) {
            return new OkHttpClient.Builder()
                    .authenticator(authenticator)
                    .build();
        }
        return new OkHttpClient();
    }

    @Bean
    Converter.Factory getConverterFactory() {
        return JacksonConverterFactory.create();
    }

    @Bean
    Retrofit getRetrofit(OkHttpClient client, Converter.Factory factory) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(factory)
                .build();
    }

}
