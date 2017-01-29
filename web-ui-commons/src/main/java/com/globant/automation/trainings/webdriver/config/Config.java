package com.globant.automation.trainings.webdriver.config;


import com.globant.automation.trainings.webdriver.browsers.Browser;

import java.util.Set;

/**
 * @author Juan Krzemien
 */
public interface Config {

    /**
     * Flag indicating whether framework should run in DEBUG logging level or not
     *
     * @return true if DEBUG logging level is set, false otherwise
     */
    boolean isDebugMode();

    WebDriver WebDriver();

    Driver Driver(Browser browser);

    /**
     * HTTP(S) Proxy configuration retrieval from config file
     *
     * @return A {@link Proxy} implementation
     */
    Proxy Proxy();

    Set<Browser> AvailableDrivers();

}
