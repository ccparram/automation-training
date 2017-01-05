package frameworks.tests;

import frameworks.runner.AutomationFramework;
import frameworks.runner.ContainerAwareRunnerFactory;
import frameworks.web.BasePageObject;
import frameworks.web.Browser;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static frameworks.config.Framework.CONFIGURATION;

/**
 * @author Juan Krzemien
 */
@RunWith(AutomationFramework.class)
@UseParametersRunnerFactory(ContainerAwareRunnerFactory.class)
public abstract class BaseTest<T extends BasePageObject> {

    @Parameter
    public Browser currentBrowser;

    @Autowired
    private T initialPage;

    @Parameters(name = "Browser {0}")
    public static Set<Browser> getBrowsers() {
        return CONFIGURATION.AvailableDrivers();
    }

    @After
    public void tearDown() {
        initialPage.dispose();
    }

    public T getInitialPage() {
        return initialPage;
    }

}
