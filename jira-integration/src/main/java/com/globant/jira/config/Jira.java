package com.globant.jira.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Juan Krzemien
 */
public class Jira {

    @JsonIgnore
    static final Jira DEFAULT = new Jira();

    @JsonProperty
    private String baseUrl = "https://jira.corp.globant.com/";

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    @JsonProperty
    private String appVersion = "";

    @JsonProperty
    private boolean proxyEnabled = false;

    @JsonProperty
    private String proxyHost = "http://proxy.corp.globant.com";

    @JsonProperty
    private int proxyPort = 3128;

    private Jira() {

    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public boolean isProxyEnabled() {
        return proxyEnabled;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }
}
