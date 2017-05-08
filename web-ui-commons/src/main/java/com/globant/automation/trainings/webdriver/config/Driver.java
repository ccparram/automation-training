package com.globant.automation.trainings.webdriver.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;

/**
 * @author Juan Krzemien
 */

public class Driver {

    public static final Driver DEFAULT = new Driver();

    @JsonProperty
    private Map<String, Object> capabilities;

    @JsonProperty
    private List<String> arguments;

    private Driver() {
    }

    public Map<String, Object> getCapabilities() {
        return ofNullable(capabilities).orElse(emptyMap());
    }

    public List<String> getArguments() {
        return ofNullable(arguments).orElse(emptyList());
    }

}
