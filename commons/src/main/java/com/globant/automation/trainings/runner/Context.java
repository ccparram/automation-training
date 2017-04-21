package com.globant.automation.trainings.runner;


import com.globant.automation.trainings.languages.Language;

/**
 * @author Juan Krzemien
 */
public interface Context {

    void init();

    void destroy();

    Language getLanguage();

    String getToken();

    void setToken(String token);

}
