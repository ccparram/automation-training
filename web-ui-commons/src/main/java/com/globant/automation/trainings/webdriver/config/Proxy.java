package com.globant.automation.trainings.webdriver.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Juan Krzemien
 */
public class Proxy {

    public static final Proxy DEFAULT = new Proxy();

    @JsonProperty
    private boolean enabled = false;

    @JsonProperty
    private String host = "";

    @JsonProperty
    private int port = 0;

    private Proxy() {
    }

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
