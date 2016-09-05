package com.globant.automation.trainings.frameworks.webdriver.webframework.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.globant.automation.trainings.frameworks.webdriver.webframework.config.interfaces.IProxy;

/**
 * @author Juan Krzemien
 */
@JsonSerialize
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
