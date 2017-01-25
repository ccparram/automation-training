package com.globant.automation.trainings.webdriver.config;

/**
 * @author Juan Krzemien
 */
public interface Proxy {

    boolean isEnabled();

    String getHost();

    int getPort();
}
