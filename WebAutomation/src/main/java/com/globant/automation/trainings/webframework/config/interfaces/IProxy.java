package com.globant.automation.trainings.webframework.config.interfaces;

/**
 * @author Juan Krzemien
 */
public interface IProxy {

    /**
     * Use HTTP(S) proxy in WebDriver flag
     *
     * @return true if WebDriver should use proxy, false otherwise.
     */
    boolean isEnabled();

    /**
     * HTTP(S) proxy host
     *
     * @return Proxy host to use defined in config file
     */
    String getHost();

    /**
     * HTTP(S) proxy port
     *
     * @return Proxy port to use defined in config file
     */
    int getPort();

}
