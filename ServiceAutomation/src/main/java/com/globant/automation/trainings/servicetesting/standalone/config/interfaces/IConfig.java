package com.globant.automation.trainings.servicetesting.standalone.config.interfaces;


import java.net.URL;
import java.util.Optional;

/**
 * @author Juan Krzemien
 */
public interface IConfig {

    /**
     * Flag indicating whether framework should run in DEBUG logging level or not
     *
     * @return true if DEBUG logging level is set, false otherwise
     */
    boolean isDebugMode();

    /**
     * HTTP(S) Proxy configuration retrieval from config file
     *
     * @return A {@link IProxy} implementation
     */
    IProxy Proxy();

    /**
     * Holds the definition of the base URL to test against
     *
     * @return URL instance
     */
    Optional<URL> getBaseUrl();
}
