package com.globant.jira.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Juan Krzemien
 */
public class Evidence {

    @JsonProperty
    public String data;

    @JsonProperty
    public String filename;

    @JsonProperty
    public String contentType;

}
