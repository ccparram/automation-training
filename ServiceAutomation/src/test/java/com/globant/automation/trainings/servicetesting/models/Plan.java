package com.globant.automation.trainings.servicetesting.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Plan {

    @JsonProperty("name")
    public String name;

    @JsonProperty("space")
    public Integer space;

    @JsonProperty("private_repos")
    public Integer privateRepos;

    @JsonProperty("collaborators")
    public Integer collaborators;

}
