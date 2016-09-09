package com.globant.automation.trainings.frameworks.webdriver.webframework.tests;

import com.globant.automation.trainings.frameworks.webdriver.webframework.logging.Logging;
import com.globant.automation.trainings.frameworks.webdriver.webframework.pageobject.PageObject;
import com.globant.automation.trainings.frameworks.webdriver.webframework.webdriver.server.SeleniumServerStandAlone;

/**
 * @author Juan Krzemien
 */
public abstract class GenericWebDriverTest<T extends PageObject> implements Logging {

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown, "SeleniumServer-Thread"));
    }

}
