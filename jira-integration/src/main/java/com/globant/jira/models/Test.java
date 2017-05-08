package com.globant.jira.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class Test {

    @JsonProperty
    public String testKey;

    @JsonProperty
    public String status;

    @JsonProperty
    public String comment;

    @JsonProperty
    public String start;

    @JsonProperty
    public String finish;

    @JsonProperty
    public List<Evidence> evidences = new ArrayList<>();

    @JsonProperty
    public List<Result> results = new ArrayList<>();

    @JsonProperty
    public List<String> examples = new ArrayList<>();

    @JsonProperty
    public List<Step> steps = new ArrayList<>();

    public static Test from(Test t) {
        Test c = new Test();
        c.status = t.status;
        c.finish = t.finish;
        c.start = t.start;
        c.comment = t.comment;
        c.testKey = t.testKey;
        c.evidences = new ArrayList<>(t.evidences);
        c.examples = new ArrayList<>(t.examples);
        c.steps = new ArrayList<>(t.steps);
        c.results = new ArrayList<>(t.results);
        return c;
    }


}
