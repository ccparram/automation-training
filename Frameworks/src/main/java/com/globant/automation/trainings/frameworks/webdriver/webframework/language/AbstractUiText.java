package com.globant.automation.trainings.frameworks.webdriver.webframework.language;

import com.globant.automation.trainings.frameworks.webdriver.webframework.logging.Logging;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Base class that provides methods for retrieval of a value given a key.
 * <p>
 * Useful for externalizing constants and strings from source code while supporting localization.
 * <p>
 * Relies entirely on Java's ResourceBundle.
 * <p>
 * Cannot be instantiated on its own. Needs to be inherited.
 *
 * @author Juan Krzemien
 */
public class AbstractUiText implements Logging {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("UiText", Locale.getDefault());

    protected AbstractUiText() {
    }

    protected static String getFor(String key) {
        return resourceBundle.getString(key);
    }
}