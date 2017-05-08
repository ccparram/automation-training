package com.globant.jira.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Juan Krzemien
 */
@JsonInclude(NON_NULL)
public class Info {

    @JsonProperty
    public String project;

    @JsonProperty
    public String summary;

    @JsonProperty
    public String description;

    @JsonProperty
    public String user;

    @JsonProperty
    public String version;

    @JsonProperty
    public String revision;

    @JsonProperty
    public String startDate;

    @JsonProperty
    public String finishDate;

}
