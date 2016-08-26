package com.globant.automation.trainings.frameworks.webdriver.tests.language;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * You can extend this class to provide your tests with externalized & localized texts/constants.
 * See WebDriverTests for an usage example.
 *
 * Created by jkrzemien on 23/08/2016.
 */
public abstract class AbstractUiText {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("UiText", Locale.getDefault());

    protected static String getFor(String key) {
        return resourceBundle.getString(key);
    }
}

