package com.globant.automation.trainings.servicetesting.github.tests.spring.context;

import com.globant.automation.trainings.servicetesting.authenticators.TokenAuthentication;
import com.globant.automation.trainings.servicetesting.github.models.Label;
import com.globant.automation.trainings.servicetesting.github.tests.spring.repositories.LabelsRepository;
import com.globant.automation.trainings.servicetesting.spring.AbstractTestContext;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Spring context definition as Java Config file.
 * <p>
 * Extends AbstractTestContext to have multi configuration settings from Spring's Profiles.
 * <p>
 * Scans Models and Repositories packages via @EntityScan and @EnableJpaRepositories, respectively.
 *
 * @author Juan Krzemien
 */
@EntityScan(basePackageClasses = Label.class)
@EnableJpaRepositories(basePackageClasses = LabelsRepository.class)
public class TestContext extends AbstractTestContext {

    @Bean
    Authenticator getAuthenticator(@Value("${token}") String token) {
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
    Retrofit getRetrofit(OkHttpClient client, Converter.Factory factory, @Value("${baseUrl}") String baseUrl) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(factory)
                .build();
    }

}
