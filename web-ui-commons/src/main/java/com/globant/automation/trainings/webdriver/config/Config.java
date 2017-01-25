package com.globant.automation.trainings.webdriver.config;


import com.globant.automation.trainings.webdriver.browsers.Browser;

import java.util.Set;

/**
 * @author Juan Krzemien
 */
public interface Config {

    boolean isDebugMode();

    WebDriver WebDriver();

    Driver Driver(Browser browser);

    Proxy Proxy();

    Set<Browser> AvailableDrivers();

}
