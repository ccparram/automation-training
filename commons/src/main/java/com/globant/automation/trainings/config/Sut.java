package com.globant.automation.trainings.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * @author Juan Krzemien
 */
class Sut {

    static final Sut DEFAULT = new Sut();

    @JsonProperty
    private String activeEnvironment = "QA";

    @JsonProperty
    private List<Environment> environments = singletonList(Environment.DEFAULT);

    private Sut() {
    }

    Environment getActiveEnvironment() {
        String fromEnvironment = System.getenv("SUT_ENVIRONMENT");
        return environments.stream().filter(e -> e.getName().equals(fromEnvironment)).findFirst()
                .orElse(environments.stream().filter(e -> e.getName().equals(activeEnvironment)).findFirst()
                        .orElse(Environment.DEFAULT));
    }
}

