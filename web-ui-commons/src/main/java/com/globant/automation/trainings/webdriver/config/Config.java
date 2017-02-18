package com.globant.automation.trainings.webdriver.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.languages.Language;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;
import static java.lang.System.getProperty;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

/**
 * @author Juan Krzemien
 */
public class Config {

    @JsonIgnore
    public static final Config EMPTY = new Config();

    @JsonProperty
    private boolean isDebugMode = false;

    @JsonDeserialize(as = WebDriver.class)
    private WebDriver webdriver = WebDriver.DEFAULT;

    @JsonProperty
    private List<Language> languages;

    @JsonDeserialize(contentAs = Driver.class)
    private Map<Browser, Driver> drivers = new HashMap<>();

    @JsonDeserialize(as = Proxy.class)
    private Proxy proxy = Proxy.DEFAULT;

    @JsonDeserialize(as = Sut.class)
    private Sut sut = Sut.DEFAULT;

    Config() {
    }

    public boolean isDebugMode() {
        return parseBoolean(getProperty("DEBUG_MODE", valueOf(isDebugMode)));
    }

    public WebDriver getWebDriver() {
        return webdriver;
    }

    public Driver getDriver(Browser browser) {
        return ofNullable(drivers).orElse(emptyMap()).computeIfAbsent(browser, k -> Driver.DEFAULT);
    }

    public Proxy getProxy() {
        return proxy;
    }

    public Set<Browser> getAvailableDrivers() {
        return drivers.keySet();
    }

    public Set<Language> getAvailableLanguages() {
        return languages.stream().collect(toSet());
    }

    public Environment getActiveEnvironment() {
        return sut.getActiveEnvironment();
    }
}
