package frameworks.web;

import frameworks.runner.ThreadPoolScheduler;
import frameworks.utils.Reflections;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.TestWithParameters;

import static frameworks.utils.Reflections.injectFieldsPageObject;
import static frameworks.web.WebDriverContext.WEB_DRIVER_CONTEXT;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

/**
 * @author Juan Krzemien
 */
public class BlockJUnit4ClassRunnerWithParametersInjector extends BlockJUnit4ClassRunnerWithParameters {

    private final WebDriverProvider webDriverProvider = new WebDriverProvider();
    private Browser browser;

    public BlockJUnit4ClassRunnerWithParametersInjector(TestWithParameters test) throws InitializationError {
        super(test);
        setScheduler(new ThreadPoolScheduler());
    }

    @Override
    public Object createTest() throws Exception {
        Object test = super.createTest();
        stream(test.getClass().getDeclaredFields()).filter(Reflections::isPom).forEach(f -> injectFieldsPageObject(f, test));
        return test;
    }

    @Override
    public void run(RunNotifier notifier) {
        currentThread().setName(browser.name() + "-" + currentThread().getName());

        try {
            WEB_DRIVER_CONTEXT.set(webDriverProvider.createDriverWith(browser));
        } catch (Exception e) {
            notifier.fireTestFailure(new Failure(getDescription(), e));
            return;
        }

        getChildren().forEach(c -> runChild(c, notifier));

        WEB_DRIVER_CONTEXT.remove();
    }

    void setBrowser(Browser browser) {
        this.browser = browser;
    }

}