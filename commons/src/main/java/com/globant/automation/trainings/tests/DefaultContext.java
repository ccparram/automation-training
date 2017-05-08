package com.globant.automation.trainings.tests;

import com.globant.automation.trainings.languages.Language;
import com.globant.automation.trainings.logging.Logging;

/**
 * @author Juan Krzemien
 */
public class DefaultContext implements Context, Logging {

    private final Language language;
    private String token;

    public DefaultContext(Language language) {
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void init() {
        // Override behaviour
    }

    @Override
    public void destroy() {
        // Override behaviour
    }
}
