package com.globant.automation.trainings.webdriver.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Environment {

    public static final Environment DEFAULT = new Environment();

    @JsonProperty("name")
    private String name = "QA";

    @JsonProperty("baseUrl")
    private String baseUrl = "http://default.base.url";

    private Environment() {
    }

    public String getName() {
        return name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
