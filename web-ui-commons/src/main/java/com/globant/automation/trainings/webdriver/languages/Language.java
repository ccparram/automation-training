package com.globant.automation.trainings.webdriver.languages;

import java.util.Locale;

/**
 * @author Juan Krzemien
 */
public enum Language {

    EN("en-US"), ES("es-AR"), PT_BR("pt-BR"), PT("pt-PT"), PL("pl-PL"), DE("de-DE");

    private final Locale locale;

    Language(String locale) {
        this.locale = Locale.forLanguageTag(locale);
    }

    public Locale toLocale() {
        return locale;
    }
}
