package com.globant.automation.trainings.webdriver.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Juan Krzemien
 */
public class WebDriver {

    private static final int DEFAULT_TIMEOUT = 30;
    private static final int DEFAULT_POOLING_INTERVAL = 500;

    public static WebDriver DEFAULT = new WebDriver();

    @JsonProperty
    private int explicitTimeOut = DEFAULT_TIMEOUT;

    @JsonProperty
    private int implicitTimeOut = 0;

    @JsonProperty
    private int pageLoadTimeout = DEFAULT_TIMEOUT;

    @JsonProperty
    private int scriptTimeout = DEFAULT_TIMEOUT;

    @JsonProperty
    private int pollingEveryMs = DEFAULT_POOLING_INTERVAL;

    @JsonProperty
    private String remoteURL = "http://localhost:4444/wd/hub";

    @JsonProperty
    private boolean isSeleniumGrid = false;

    private WebDriver() {
    }

    public int getExplicitTimeOut() {
        return explicitTimeOut;
    }

    public int getImplicitTimeOut() {
        return implicitTimeOut;
    }

    public long getPageLoadTimeout() {
        return pageLoadTimeout;
    }

    public long getScriptTimeout() {
        return scriptTimeout;
    }

    public int getPollingEveryMs() {
        return pollingEveryMs;
    }

    public String getRemoteURL() {
        return remoteURL;
    }

    public boolean isSeleniumGrid() {
        return isSeleniumGrid;
    }

}
