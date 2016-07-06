package com.globant.automation.trainings.frameworks.webdriver.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.globant.automation.trainings.frameworks.webdriver.config.interfaces.IWebDriverConfig;

/**
 *
 *
 * @author Juan Krzemien
 */
@JsonSerialize
class WebDriver implements IWebDriverConfig {

    @JsonProperty
    private int implicitTimeOut = 30;

    @JsonProperty
    private int pageLoadTimeout = 30;

    @JsonProperty
    private int scriptTimeout = 30;

    @JsonProperty
    private String appiumRemoteURL = "http://127.0.0.1:4723/wd/hub";

    @JsonProperty
    private boolean useListener = true;

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
    public String getAppiumRemoteURL() {
        return appiumRemoteURL;
    }

    @Override
    public boolean isUseListener() {
        return useListener;
    }
}
