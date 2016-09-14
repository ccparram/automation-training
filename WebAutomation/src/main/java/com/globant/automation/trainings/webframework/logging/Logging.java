package com.globant.automation.trainings.webframework.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Juan Krzemien
 */
public interface Logging {
    default Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }
}
