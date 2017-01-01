package frameworks.config.interfaces;

import java.net.URL;

/**
 * @author Juan Krzemien
 */
public interface IWebDriverConfig {

    /**
     * Timeout (in seconds) for WebDriver's active waiting
     *
     * @return Integer specifying seconds
     */
    int getExplicitTimeOut();

    /**
     * Timeout (in seconds) for WebDriver's passive waiting
     *
     * @return Integer specifying seconds
     */
    int getImplicitTimeOut();

    /**
     * Timeout (in seconds) for WebDriver's time to load a page
     *
     * @return Integer specifying seconds
     */
    long getPageLoadTimeout();

    /**
     * Timeout (in seconds) for WebDriver's time to run a JavaScript snippet
     *
     * @return Integer specifying seconds
     */
    long getScriptTimeout();

    /**
     * Retry interval (in milliseconds) for WebDriver's explicit waiting
     *
     * @return Integer specifying seconds
     */
    int getPollingEveryMs();

    /**
     * URL to run WebDriver implementation against
     *
     * @return WebDriver implementation's target URL
     */
    URL getRemoteURL();

    /**
     * Flag that defines if local Selenium Server should be deployed or not.
     *
     * @return true if running against Selenium Grid, false otherwise
     */
    boolean isUseSeleniumGrid();

    /**
     * Enables Framework's WebDriver logging feature
     *
     * @return true if WebDriver logging is enabled, false otherwise
     */
    boolean isUseListener();
}
