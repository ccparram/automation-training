package com.globant.jira.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Step {

    @JsonProperty
    public String status;

    @JsonProperty
    public String comment;

    @JsonProperty
    public List<Evidence> evidences = new ArrayList<>();

}
