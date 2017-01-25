package com.globant.automation.trainings.webdriver.config;

/**
 * @author Juan Krzemien
 */
public interface WebDriver {
    int getExplicitTimeOut();

    int getImplicitTimeOut();

    long getPageLoadTimeout();

    long getScriptTimeout();

    int getPollingEveryMs();

    String getRemoteURL();

    boolean isSeleniumGrid();

    boolean isUseListener();
}
