package com.globant.automation.trainings.frameworks.webdriver.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.globant.automation.trainings.frameworks.webdriver.config.interfaces.IWebDriverConfig;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Juan Krzemien
 */
@JsonSerialize
class WebDriver implements IWebDriverConfig {

    @JsonProperty
    private int explicitTimeOut = 30;

    @JsonProperty
    private int implicitTimeOut = 1;

    @JsonProperty
    private int pageLoadTimeout = 30;

    @JsonProperty
    private int scriptTimeout = 30;

    @JsonProperty
    private int pollingEveryMs = 500;

    @JsonProperty
    private URL remoteURL = new URL("http://localhost:4444/wd/hub");

    @JsonProperty
    private boolean useSeleniumGrid = false;

    @JsonProperty
    private boolean useListener = true;

    WebDriver() throws MalformedURLException {
    }

    @Override
    public int getExplicitTimeOut() {
        return explicitTimeOut;
    }

    @Override
    public int getImplicitTimeOut() {
        return implicitTimeOut;
    }

    @Override
    public long getPageLoadTimeout() {
        return pageLoadTimeout;
    }

    @Override
    public long getScriptTimeout() {
        return scriptTimeout;
    }

    @Override
    public int getPollingEveryMs() {
        return pollingEveryMs;
    }

    @Override
    public URL getRemoteURL() {
        return remoteURL;
    }

    @Override
    public boolean isUseSeleniumGrid() {
        return useSeleniumGrid;
    }

    @Override
    public boolean isUseListener() {
        return useListener;
    }
}
