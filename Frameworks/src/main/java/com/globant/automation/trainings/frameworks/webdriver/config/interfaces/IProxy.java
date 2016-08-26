package com.globant.automation.trainings.frameworks.webdriver.config.interfaces;

/**
 * @author Juan Krzemien
 */
public interface IProxy {

    boolean isEnabled();

    String getHost();

    int getPort();

}
