package frameworks.web;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.utils.Reflection;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.config.Framework;
import com.globant.automation.trainings.runner.Parallelism;
import com.globant.automation.trainings.webdriver.server.SeleniumServerStandAlone;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.globant.automation.trainings.utils.Reflection.injectFieldsPageObject;
import static frameworks.web.WebDriverContext.WEB_DRIVER_CONTEXT;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;
import static org.junit.runner.Description.createTestDescription;

public class WebDriverRunner extends Parallelism implements Logging {

    private final WebDriverProvider webDriverProvider = new WebDriverProvider();

    public WebDriverRunner(Class<?> clazz) throws Throwable {
        super(clazz);
        Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown));
    }

    @Override
    public Description getDescription() {
        return Description.EMPTY;
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        final List<FrameworkMethod> methods = super.getChildren();
        final Set<Browser> browsers = Framework.CONFIGURATION.AvailableDrivers();
        final List<FrameworkMethod> expandedMethods = new ArrayList<>(methods.size() * browsers.size());
        methods.forEach(m -> browsers.forEach(b -> expandedMethods.add(new WebDriverFrameworkMethod(m, b))));
        return expandedMethods;
    }

    @Override
    protected Object createTest() throws Exception {
        final Object test = super.createTest();
        stream(test.getClass().getDeclaredFields()).filter(f -> Reflection.isSubClassOf(f, PageObject.class)).forEach(f -> injectFieldsPageObject(f, test));
        return test;
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {

        final Browser browser = ((WebDriverFrameworkMethod) method).getBrowser();

        currentThread().setName(browser.name() + "-" + currentThread().getName());

        try {
            WEB_DRIVER_CONTEXT.set(webDriverProvider.createDriverWith(browser));
        } catch (MalformedURLException e) {
            notifier.fireTestFailure(new Failure(getDescription(), e));
            return;
        }

        final Description description = createTestDescription(getTestClass().getJavaClass(), browser.name() + "-" + method.getName());

        runLeaf(methodBlock(method), description, notifier);

        WEB_DRIVER_CONTEXT.remove();
    }

}
