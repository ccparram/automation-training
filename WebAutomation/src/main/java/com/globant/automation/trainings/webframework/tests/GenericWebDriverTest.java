package com.globant.automation.trainings.webframework.tests;

import com.globant.automation.trainings.webframework.logging.Logging;
import com.globant.automation.trainings.webframework.pageobject.PageObject;
import com.globant.automation.trainings.webframework.webdriver.server.SeleniumServerStandAlone;

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
