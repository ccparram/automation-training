package com.globant.automation.trainings.servicetesting.models;

import com.fasterxml.jackson.annotation.JsonProperty;

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
