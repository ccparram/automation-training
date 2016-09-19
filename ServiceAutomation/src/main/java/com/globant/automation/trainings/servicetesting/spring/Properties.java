package com.globant.automation.trainings.servicetesting.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Juan Krzemien
 */
@Configuration
public class Properties {

    @Profile({"dev"})
    @Configuration
    @PropertySource("classpath:dev.properties")
    public static class devConfig {
    }

    @Profile({"qa", "default"})
    @Configuration
    @PropertySource("classpath:qa.properties")
    public static class qaConfig {
    }

    @Profile("staging")
    @Configuration
    @PropertySource("classpath:staging.properties")
    public static class stagingConfig {
    }
}
