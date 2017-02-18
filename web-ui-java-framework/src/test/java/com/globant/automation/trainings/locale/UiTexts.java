package com.globant.automation.trainings.locale;


import com.globant.automation.trainings.webdriver.texts.AbstractUITexts;

/**
 * Example of a class that allows to look up a string literal for different locales (languages) with a single keyword.
 *
 * @author Juan Krzemien
 */
public class UiTexts extends AbstractUITexts {

    UiTexts() {
    }

    public static class Google {
        public static String Something() {
            return $("SOMETHING");
        }
    }
}
