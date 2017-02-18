package com.globant.automation.trainings.webdriver.texts;


import com.globant.automation.trainings.webdriver.tests.TestContext;

import java.util.Random;
import java.util.ResourceBundle;

import static java.util.Optional.ofNullable;
import static java.util.ResourceBundle.getBundle;

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
public abstract class AbstractUITexts {

    protected static final Random rnd = new Random();

    protected static String $(String key) {
        return ofNullable(TestContext.get())
                .map(ctx -> ResourceBundle.getBundle("UITexts", ctx.getLanguage().toLocale()).getString(key))
                .orElseGet(() -> getBundle("UITexts").getString(key));
    }

}
