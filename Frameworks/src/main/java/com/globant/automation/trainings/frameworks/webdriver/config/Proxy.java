package com.globant.automation.trainings.frameworks.webdriver.config;

import com.globant.automation.trainings.frameworks.webdriver.config.interfaces.IProxy;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 *
 * @author Juan Krzemien
 */
@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
@JsonPropertyOrder({"enabled", "host", "port"})
class Proxy implements IProxy {

    @JsonProperty
    private boolean enabled = false;

    @JsonProperty
    private String host = "";

    @JsonProperty
    private int port = 0;

    public boolean isEnabled() {
        return enabled;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}
