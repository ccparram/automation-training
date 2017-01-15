package com.globant.automation.trainings.webframework.tests;

import com.globant.automation.trainings.runners.junit.Parallelism;
import com.globant.automation.trainings.webframework.events.Messages;
import com.globant.automation.trainings.webframework.pageobject.PageObject;
import com.globant.automation.trainings.webframework.webdriver.Browser;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Set;

import static com.globant.automation.trainings.webframework.config.Framework.CONFIGURATION;
import static com.globant.automation.trainings.webframework.events.EventBus.FRAMEWORK;

/**
 * This sample class is the entry point for JUnit tests to the framework.
 *
 * @author Juan Krzemien
 */
@RunWith(Parallelism.class)
public abstract class JUnit4WebDriverTest<T extends PageObject> extends GenericWebDriverTest<T> {

    @Parameter
    public Browser currentBrowser;

    @Parameterized.Parameters(name = "Browser {0}")
    public static Set<Browser> getBrowsers() {
        return CONFIGURATION.AvailableDrivers();
    }

    @Before
    public void setUp() {
        FRAMEWORK.post(Messages.Test.START(currentBrowser));
    }

    @After
    public void tearDown() {
        FRAMEWORK.post(Messages.Test.FINISH(currentBrowser));
    }
}

