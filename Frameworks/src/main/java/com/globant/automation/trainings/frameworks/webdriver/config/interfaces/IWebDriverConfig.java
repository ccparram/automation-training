package com.globant.automation.trainings.frameworks.webdriver.config.interfaces;

import java.net.URL;

/**
 * @author Juan Krzemien
 */
public interface IWebDriverConfig {

    int getExplicitTimeOut();

    int getImplicitTimeOut();

    long getPageLoadTimeout();

    long getScriptTimeout();

    int getPollingEveryMs();

    URL getRemoteURL();

    boolean isUseSeleniumGrid();

    boolean isUseListener();
}
