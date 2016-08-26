package com.globant.automation.trainings.frameworks.webdriver.tests;

import com.globant.automation.trainings.frameworks.webdriver.config.Framework;
import com.globant.automation.trainings.frameworks.webdriver.selenium.SeleniumServerStandAlone;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Juan Krzemien
 */
public abstract class GenericWebDriverTest<T extends PageObject> {

    protected final static Logger LOG = getLogger(Framework.class);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown));
    }

}
