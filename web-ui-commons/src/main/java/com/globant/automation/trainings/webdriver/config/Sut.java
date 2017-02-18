package com.globant.automation.trainings.webdriver.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * @author Juan Krzemien
 */
class Sut {

    static final Sut DEFAULT = new Sut();

    @JsonProperty("activeEnvironment")
    private String activeEnvironment = "QA";

    @JsonProperty("environments")
    private List<Environment> environments = Collections.singletonList(Environment.DEFAULT);

    private Sut() {
    }

    Environment getActiveEnvironment() {
        String fromEnvironment = System.getenv("SUT_ENVIRONMENT");
        return environments.stream().filter(e -> e.getName().equals(fromEnvironment)).findFirst()
                .orElse(environments.stream().filter(e -> e.getName().equals(activeEnvironment)).findFirst()
                        .orElse(Environment.DEFAULT));
    }
}

