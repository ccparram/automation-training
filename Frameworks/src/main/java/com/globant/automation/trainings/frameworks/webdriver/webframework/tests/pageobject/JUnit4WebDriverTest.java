package com.globant.automation.trainings.frameworks.webdriver.webframework.tests.pageobject;

import com.globant.automation.trainings.frameworks.junit.Parallelism;
import com.globant.automation.trainings.frameworks.webdriver.webframework.enums.Browser;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Set;

import static com.globant.automation.trainings.frameworks.webdriver.webframework.config.Framework.CONFIGURATION;

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
        Drivers.INSTANCE.create(currentBrowser);
    }

    @After
    public void tearDown() {
        Drivers.INSTANCE.destroy();
    }
}

