package com.globant.automation.trainings.webframework.tests;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.server.SeleniumServerStandAlone;
import com.globant.automation.trainings.webframework.pageobject.PageObject;

/**
 * @author Juan Krzemien
 */
public abstract class GenericWebDriverTest<T extends PageObject> implements Logging {

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown, "SeleniumServer-Thread"));
    }

    protected GenericWebDriverTest() {

    }
}
