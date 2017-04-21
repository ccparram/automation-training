package com.globant.automation.trainings.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.globant.automation.trainings.languages.Language;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;
import static java.lang.System.getProperty;

/**
 * @author Juan Krzemien
 */
public class Config {

    @JsonIgnore
    public static final Config EMPTY = new Config();

    @JsonProperty
    private boolean isDebugMode = false;

    @JsonProperty
    private List<Language> languages;

    @JsonDeserialize(as = Proxy.class)
    private Proxy proxy = Proxy.DEFAULT;

    @JsonDeserialize(as = Sut.class)
    private Sut sut = Sut.DEFAULT;

    private Config() {
    }

    public boolean isDebugMode() {
        return parseBoolean(getProperty("DEBUG_MODE", valueOf(isDebugMode)));
    }

    public Proxy getProxy() {
        return proxy;
    }

    public Set<Language> getAvailableLanguages() {
        return new HashSet<>(languages);
    }

    public Environment getActiveEnvironment() {
        return sut.getActiveEnvironment();
    }

}
