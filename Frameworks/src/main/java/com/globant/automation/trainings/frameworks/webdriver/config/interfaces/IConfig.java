package com.globant.automation.trainings.frameworks.webdriver.config.interfaces;


import com.globant.automation.trainings.frameworks.webdriver.enums.Browser;

import java.util.Set;

/**
 *
 *
 * @author Juan Krzemien
 */
public interface IConfig {
    IWebDriverConfig WebDriver();

    IDriver Driver(Browser browser);

    IProxy Proxy();

    Set<Browser> AvailableDrivers();
}
