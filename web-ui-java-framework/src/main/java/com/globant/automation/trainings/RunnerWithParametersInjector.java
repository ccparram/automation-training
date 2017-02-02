package com.globant.automation.trainings;

import com.globant.automation.trainings.runner.ThreadPoolScheduler;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.config.Framework;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.TestWithParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.globant.automation.trainings.utils.Reflection.injectFieldOwnInstance;
import static com.globant.automation.trainings.utils.Reflection.fieldIsSubClassOf;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

/**
 * @author Juan Krzemien
 */
class RunnerWithParametersInjector extends BlockJUnit4ClassRunnerWithParameters {

    private final WebDriverProvider webDriverProvider = new WebDriverProvider();

    RunnerWithParametersInjector(TestWithParameters test) throws InitializationError {
        super(test);
        setScheduler(new ThreadPoolScheduler());
    }

    @Override
    public Object createTest() throws Exception {
        Object test = super.createTest();
        stream(test.getClass().getDeclaredFields()).filter(f -> fieldIsSubClassOf(f, PageObject.class)).forEach(f -> injectFieldOwnInstance(f, test));
        return test;
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        final List<FrameworkMethod> frameworkMethods = super.getChildren();
        final Set<Browser> browsers = Framework.CONFIGURATION.AvailableDrivers();
        final List<FrameworkMethod> expandedMethods = new ArrayList<>(frameworkMethods.size() * browsers.size());
        frameworkMethods.forEach(f -> browsers.forEach(b -> expandedMethods.add(new WebDriverFrameworkMethod(f, b))));
        return expandedMethods;
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        Browser browser = ((WebDriverFrameworkMethod) method).getBrowser();

        if (browser == null) {
            throw new IllegalStateException("Browser is null! setBrowser() first!. Cannot run WebDriver test!");
        }

        currentThread().setName(browser.name() + "-" + currentThread().getName());

        try {
            WebDriverContext.WEB_DRIVER_CONTEXT.set(new WebDriverContext.BrowserDriverPair(browser, webDriverProvider.createDriverWith(browser)));
            super.runChild(method, notifier);
        } catch (Exception e) {
            notifier.fireTestFailure(new Failure(getDescription(), e));
        } finally {
            WebDriverContext.WEB_DRIVER_CONTEXT.remove();
        }
    }

}