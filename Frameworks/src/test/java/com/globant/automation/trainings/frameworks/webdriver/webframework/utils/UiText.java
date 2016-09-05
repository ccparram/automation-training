package com.globant.automation.trainings.frameworks.webdriver.webframework.utils;

import com.globant.automation.trainings.frameworks.webdriver.webframework.tests.language.AbstractUiText;

/**
 * @author Juan Krzemien
 */
public class UiText extends AbstractUiText {

    UiText() {
    }

    public enum Constants {

        SOMETHING;

        @Override
        public String toString() {
            return getFor(name());
        }
    }

}
