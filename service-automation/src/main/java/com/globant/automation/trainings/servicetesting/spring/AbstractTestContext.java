package com.globant.automation.trainings.servicetesting.spring;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Juan Krzemien
 */
@Configuration
@EnableAutoConfiguration
public abstract class AbstractTestContext {

    @Profile({"dev"})
    @Configuration
    @PropertySource("classpath:spring/dev.properties")
    public static class devConfig {
    }

    @Profile({"qa", "default"})
    @Configuration
    @PropertySource("classpath:spring/qa.properties")
    public static class qaConfig {
    }

    @Profile("staging")
    @Configuration
    @PropertySource("classpath:spring/staging.properties")
    public static class stagingConfig {
    }

}
