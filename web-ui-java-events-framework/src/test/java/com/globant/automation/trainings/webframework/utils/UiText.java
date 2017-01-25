package com.globant.automation.trainings.webframework.utils;


import com.globant.automation.trainings.language.AbstractUiText;
import com.globant.automation.trainings.logging.Logging;

import java.util.Locale;

import static java.lang.String.format;

/**
 * Example of a class that allows to look up a string literal for different locales (languages) with a single keyword.
 *
 * @author Juan Krzemien
 */
public class UiText extends AbstractUiText {

    UiText() {
    }

    public enum Constants implements Logging {

        SOMETHING;

        @Override
        public String toString() {
            getLogger().info(format("Looking up [%s] in localized constants for [%s]...", name(), Locale.getDefault()));
            return getFor(name());
        }
    }

}
