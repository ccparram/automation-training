package com.globant.automation.trainings.webframework.tests;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.server.SeleniumServerStandAlone;
import com.globant.automation.trainings.webframework.events.Messages;
import com.globant.automation.trainings.webframework.pageobject.PageObject;
import com.globant.automation.trainings.webframework.webdriver.DriverEventsHandler;

import static com.globant.automation.trainings.webframework.events.EventBus.FRAMEWORK;

/**
 * @author Juan Krzemien
 */
public abstract class GenericWebDriverTest<T extends PageObject> implements Logging {

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown, "SeleniumServer-Thread"));
    }

    protected GenericWebDriverTest() {
        DriverEventsHandler.INSTANCE.init();
    }

    protected void setUp(Browser browser) {
        FRAMEWORK.post(Messages.Test.START(browser));
    }

    protected void tearDown(Browser browser) {
        FRAMEWORK.post(Messages.Test.FINISH(browser));
    }
}
