package com.globant.automation.trainings.frameworks.webdriver.config.interfaces;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 *
 * @author Juan Krzemien
 */
@JsonSerialize
@JsonPropertyOrder({"enabled", "host", "port"})
public interface IProxy {

    boolean isEnabled();

    String getHost();

    int getPort();

}
