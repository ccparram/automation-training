package com.globant.automation.trainings.webframework.config.interfaces;


import com.globant.automation.trainings.webframework.webdriver.Browser;

import java.util.Set;

/**
 * @author Juan Krzemien
 */
public interface IConfig {

    /**
     * Flag indicating whether framework should run in DEBUG logging level or not
     *
     * @return true if DEBUG logging level is set, false otherwise
     */
    boolean isDebugMode();

    /**
     * Global WebDriver specific configuration/tuning retrieval from config file
     *
     * @return A {@link IWebDriverConfig} implementation
     */
    IWebDriverConfig WebDriver();

    /**
     * Browser's driver specific configuration/capabilities retrieval from config file
     *
     * @param browser The browser type to get information for
     * @return A {@link IDriver} implementation
     */
    IDriver Driver(Browser browser);

    /**
     * WebDriver's HTTP(S) Proxy configuration retrieval from config file
     *
     * @return A {@link IProxy} implementation
     */
    IProxy Proxy();

    /**
     * Declared browsers retrieval from config file
     *
     * @return A {@link IWebDriverConfig} implementation
     */
    Set<Browser> AvailableDrivers();
}
