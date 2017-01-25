package com.globant.automation.trainings.webdriver.config.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.config.Config;
import com.globant.automation.trainings.webdriver.config.Driver;
import com.globant.automation.trainings.webdriver.config.Proxy;
import com.globant.automation.trainings.webdriver.config.WebDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;
import static java.lang.System.getProperty;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;

/**
 * @author Juan Krzemien
 */
@JsonSerialize
public class ConfigImpl implements Config {

    @JsonIgnore
    public static final Config EMPTY = new ConfigImpl();

    @JsonProperty
    private boolean isDebugMode = false;

    @JsonDeserialize(as = WebDriverImpl.class)
    private WebDriver webdriver = new WebDriverImpl();

    @JsonDeserialize(contentAs = DriverImpl.class)
    private Map<Browser, Driver> drivers = new HashMap<>();

    @JsonDeserialize(as = ProxyImpl.class)
    private Proxy proxy = new ProxyImpl();

    ConfigImpl() {
    }

    @Override
    public boolean isDebugMode() {
        return parseBoolean(getProperty("DEBUG_MODE", valueOf(isDebugMode)));
    }

    @Override
    public WebDriver WebDriver() {
        return webdriver;
    }

    @Override
    public Driver Driver(Browser browser) {
        return ofNullable(drivers).orElse(emptyMap()).computeIfAbsent(browser, k -> new DriverImpl());
    }

    @Override
    public Proxy Proxy() {
        return proxy;
    }

    @Override
    public Set<Browser> AvailableDrivers() {
        return drivers.keySet();
    }
}
