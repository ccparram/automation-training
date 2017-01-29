package com.globant.automation.trainings.servicetesting.config;


import java.net.URL;
import java.util.Optional;

/**
 * @author Juan Krzemien
 */
public interface Config {

    /**
     * Flag indicating whether framework should run in DEBUG logging level or not
     *
     * @return true if DEBUG logging level is set, false otherwise
     */
    boolean isDebugMode();

    /**
     * HTTP(S) Proxy configuration retrieval from config file
     *
     * @return A {@link Proxy} implementation
     */
    Proxy Proxy();

    /**
     * Holds the definition of the base URL to test against
     *
     * @return URL instance
     */
    Optional<URL> getBaseUrl();
}
