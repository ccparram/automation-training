package com.globant.automation.trainings.webframework.config.interfaces;

import java.util.List;
import java.util.Map;

/**
 * @author Juan Krzemien
 */
public interface IDriver {

    /**
     * Path to browser's deployed server
     *
     * @return String representing the driver's absolute path
     */
    String getDriverServerPath();

    /**
     * Retrieves capabilities defined in config file for this particular browser
     *
     * @return Map with key/value pairs of capabilities for this particular browser
     */
    Map<String, Object> getCapabilities();

    /**
     * Retrieves arguments defined in config file for this particular browser
     *
     * @return List of strings with arguments for this particular browser
     */
    List<String> getArguments();
}
