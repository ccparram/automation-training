package com.globant.automation.trainings.servicetesting.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Juan Krzemien
 */
public class Label {

    @JsonProperty
    public String url;

    @JsonProperty
    public String name;

    @JsonProperty
    public String color;

}
