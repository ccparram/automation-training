package com.globant.jira.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Result {

    @JsonProperty
    public String name;

    @JsonProperty
    public Object duration;

    @JsonProperty
    public String log;

    @JsonProperty
    public String status;

    @JsonProperty
    public List<String> examples = new ArrayList<>();

}
