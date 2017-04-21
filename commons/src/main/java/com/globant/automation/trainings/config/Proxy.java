package com.globant.automation.trainings.config;

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
    private int port = 80;

    @JsonProperty
    private String noProxyFor = "";

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

    public String getNoProxyFor() {
        return noProxyFor;
    }
}
