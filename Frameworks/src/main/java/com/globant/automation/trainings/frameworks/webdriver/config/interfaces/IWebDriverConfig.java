package com.globant.automation.trainings.frameworks.webdriver.config.interfaces;

/**
 *
 *
 * @author Juan Krzemien
 */
public interface IWebDriverConfig {
    int getImplicitTimeOut();

    long getPageLoadTimeout();

    long getScriptTimeout();

    String getAppiumRemoteURL();

    boolean isUseListener();
}
