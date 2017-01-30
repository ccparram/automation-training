package com.globant.automation.trainings.servicetesting.github.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * No getters/setters for brevity
 *
 * @author Juan Krzemien
 */
public class Plan {

    @JsonProperty
    public String name;

    @JsonProperty
    public Integer space;

    @JsonProperty("private_repos")
    public Integer privateRepos;

    @JsonProperty
    public Integer collaborators;

}
