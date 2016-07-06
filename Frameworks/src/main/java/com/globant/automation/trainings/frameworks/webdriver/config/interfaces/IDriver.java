package com.globant.automation.trainings.frameworks.webdriver.config.interfaces;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Juan Krzemien
 */
public interface IDriver {

    String getPath();

    Map<String, Object> getCapabilities();

    List<String> getArguments();
}