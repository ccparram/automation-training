package com.globant.jira.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Juan Krzemien
 */
@JsonInclude(NON_NULL)
public class ExecutionResults {

    @JsonProperty
    public String testExecutionKey;

    @JsonProperty
    public Info info = new Info();

    @JsonProperty
    public List<Test> tests = new ArrayList<>();

}

