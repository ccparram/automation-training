package com.globant.automation.trainings.webdriver.config.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.globant.automation.trainings.webdriver.config.Proxy;

/**
 * @author Juan Krzemien
 */
@JsonSerialize
class ProxyImpl implements Proxy {

    @JsonProperty
    private boolean enabled = false;

    @JsonProperty
    private String host = "";

    @JsonProperty
    private int port = 0;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

}
