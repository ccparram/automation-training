package frameworks.web;

import frameworks.config.Framework;
import frameworks.runner.ThreadPoolScheduler;
import frameworks.utils.Reflection;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.TestWithParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static frameworks.utils.Reflection.injectFieldsPageObject;
import static frameworks.web.WebDriverContext.WEB_DRIVER_CONTEXT;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

/**
 * @author Juan Krzemien
 */
public class RunnerWithParametersInjector extends BlockJUnit4ClassRunnerWithParameters {

    private final WebDriverProvider webDriverProvider = new WebDriverProvider();

    RunnerWithParametersInjector(TestWithParameters test) throws InitializationError {
        super(test);
        setScheduler(new ThreadPoolScheduler());
    }

    @Override
    public Object createTest() throws Exception {
        Object test = super.createTest();
        stream(test.getClass().getDeclaredFields()).filter(Reflection::isPom).forEach(f -> injectFieldsPageObject(f, test));
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
            WEB_DRIVER_CONTEXT.set(webDriverProvider.createDriverWith(browser));
            super.runChild(method, notifier);
        } catch (Exception e) {
            notifier.fireTestFailure(new Failure(getDescription(), e));
        } finally {
            WEB_DRIVER_CONTEXT.remove();
        }
    }

}