package com.globant.automation.trainings.servicetesting.standalone.config.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.servicetesting.standalone.config.Config;
import com.globant.automation.trainings.servicetesting.standalone.config.Proxy;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;
import static java.lang.System.getProperty;

/**
 * @author Juan Krzemien
 */
@JsonSerialize
public class ConfigImpl implements Config, Logging {

    @JsonIgnore
    public static final Config EMPTY = new ConfigImpl();

    @JsonProperty
    private boolean isDebugMode = false;

    @JsonProperty
    private ProxyImpl proxy = new ProxyImpl();

    @JsonProperty
    private String baseUrl = "";

    private ConfigImpl() {
    }

    @Override
    public boolean isDebugMode() {
        return parseBoolean(getProperty("DEBUG_MODE", valueOf(isDebugMode)));
    }

    @Override
    public Proxy Proxy() {
        return proxy;
    }

    @Override
    public URL getBaseUrl() throws MalformedURLException {
        return new URL(baseUrl);
    }

}
