package com.globant.automation.trainings.frameworks.webdriver.test;

import com.globant.automation.trainings.frameworks.junit.Parallelism;
import com.globant.automation.trainings.frameworks.webdriver.enums.Browser;
import com.globant.automation.trainings.frameworks.webdriver.factories.Drivers;
import com.globant.automation.trainings.frameworks.webdriver.test.pageobject.PageObject;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Set;

import static com.globant.automation.trainings.frameworks.webdriver.config.Framework.CONFIGURATION;

/**
 * This class is the entry point for JUnit tests to the framework.
 * When extending this class, you MUST define a public static method
 * annotated with @Parameters.
 *
 * @author Juan Krzemien
 */
@RunWith(Parallelism.class)
public abstract class JUnit4WebDriverTest<T extends PageObject> extends GenericWebDriverTest<T> {

    @Parameter
    public Browser currentBrowser;

    @Parameterized.Parameters(name = "Browser {0}")
    public static Set<Browser> getBrowsers() throws Exception {
        return CONFIGURATION.AvailableDrivers();
    }

    @Before
    public void setUp() {
        Drivers.INSTANCES.create(currentBrowser);
    }

    @After
    public void tearDown() {
        Drivers.INSTANCES.destroy();
    }
}

