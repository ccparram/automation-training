package com.globant.automation.trainings.frameworks.webdriver.webframework.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.globant.automation.trainings.frameworks.webdriver.webframework.config.interfaces.IConfig;
import com.globant.automation.trainings.frameworks.webdriver.webframework.config.interfaces.IDriver;
import com.globant.automation.trainings.frameworks.webdriver.webframework.config.interfaces.IProxy;
import com.globant.automation.trainings.frameworks.webdriver.webframework.webdriver.Browser;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;
import static java.lang.System.getProperty;

/**
 * @author Juan Krzemien
 */
@JsonSerialize
class Config implements IConfig {

    @JsonProperty
    private boolean isDebugMode = false;

    @JsonProperty
    private WebDriver webdriver = new WebDriver();

    @JsonProperty
    private Map<Browser, Driver> drivers = new HashMap<>();

    @JsonProperty
    private Proxy proxy = new Proxy();

    Config() throws MalformedURLException {
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
    public IDriver Driver(Browser browser) {
        Driver driverConfig = drivers.get(browser);
        if (driverConfig == null) {
            driverConfig = new Driver();
            drivers.put(browser, driverConfig);
        }
        return driverConfig;
    }

    @Override
    public IProxy Proxy() {
        return proxy;
    }

    public Set<Browser> AvailableDrivers() {
        return drivers.keySet();
    }
}
