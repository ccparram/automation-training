package com.globant.automation.trainings.webdriver.config;

import java.util.List;
import java.util.Map;

/**
 * @author Juan Krzemien
 */
public interface Driver {

    Map<String, Object> getCapabilities();

    List<String> getArguments();
}
