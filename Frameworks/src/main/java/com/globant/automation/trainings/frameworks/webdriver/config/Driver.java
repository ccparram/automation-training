package com.globant.automation.trainings.frameworks.webdriver.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.globant.automation.trainings.frameworks.webdriver.config.interfaces.IDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Juan Krzemien
 */

@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
@JsonPropertyOrder({"name", "capabilities"})
class Driver implements IDriver {

    @JsonProperty
    private String path = "";

    @JsonProperty
    private Map<String, Object> capabilities = new HashMap<>();

    @JsonProperty
    private List<String> arguments = new ArrayList<>();

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Map<String, Object> getCapabilities() {
        return capabilities;
    }

    @Override
    public List<String> getArguments() {
        return arguments;
    }

}
