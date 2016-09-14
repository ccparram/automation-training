package com.globant.automation.trainings.webframework.utils;


import com.globant.automation.trainings.webframework.language.AbstractUiText;
import com.globant.automation.trainings.webframework.logging.Logging;

import java.util.Locale;

import static java.lang.String.format;

/**
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
