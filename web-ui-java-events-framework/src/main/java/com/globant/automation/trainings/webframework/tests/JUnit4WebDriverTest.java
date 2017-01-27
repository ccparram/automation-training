package com.globant.automation.trainings.webframework.tests;

import com.globant.automation.trainings.runner.ParametrizedParallelism;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webframework.pageobject.PageObject;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Set;

import static com.globant.automation.trainings.webdriver.config.Framework.CONFIGURATION;

/**
 * This sample class is the entry point for JUnit tests to the framework.
 *
 * @author Juan Krzemien
 */
@RunWith(ParametrizedParallelism.class)
public abstract class JUnit4WebDriverTest<T extends PageObject> extends GenericWebDriverTest<T> {

    @Parameter
    public Browser currentBrowser;

    @Parameters(name = "Browser {0}")
    public static Set<Browser> getBrowsers() {
        return CONFIGURATION.AvailableDrivers();
    }

    @Before
    public void setUp() {
        setUp(currentBrowser);
    }

    @After
    public void tearDown() {
        tearDown(currentBrowser);
    }
}

