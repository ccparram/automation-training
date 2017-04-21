package com.globant.automation.trainings.webdriver.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.globant.automation.trainings.webdriver.browsers.Browser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;

/**
 * @author Juan Krzemien
 */
class UIConfig {

    @JsonIgnore
    static final UIConfig EMPTY = new UIConfig();

    @JsonDeserialize(as = WebDriver.class)
    private WebDriver webdriver = WebDriver.DEFAULT;

    @JsonDeserialize(contentAs = Driver.class)
    private Map<Browser, Driver> drivers = new HashMap<>();

    private UIConfig() {
    }

    WebDriver getWebDriver() {
        return webdriver;
    }

    Driver getDriver(Browser browser) {
        return ofNullable(drivers).orElse(emptyMap()).computeIfAbsent(browser, k -> Driver.DEFAULT);
    }

    Set<Browser> getAvailableDrivers() {
        return drivers.keySet();
    }

}
