package frameworks.web;

import frameworks.config.Framework;
import frameworks.logging.Logging;
import frameworks.runner.ParametrizedParallelism;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static frameworks.web.WebDriverContext.WEB_DRIVER_CONTEXT;
import static java.lang.Thread.currentThread;

public class ParametrizedWebDriverRunner extends ParametrizedParallelism implements Logging {

    private final WebDriverProvider webDriverProvider = new WebDriverProvider();

    public ParametrizedWebDriverRunner(Class<?> clazz) throws Throwable {
        super(clazz);
        Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown));
    }

    @Override
    protected List<Runner> getChildren() {
        final List<Runner> runners = super.getChildren();
        final Set<Browser> browsers = Framework.CONFIGURATION.AvailableDrivers();
        final List<Runner> expandedRunners = new ArrayList<>(runners.size() * browsers.size());
        runners.forEach(m -> browsers.forEach(b -> expandedRunners.add(new InternalWebDriverRunner(m, b))));
        return expandedRunners;
    }

    private class InternalWebDriverRunner extends Runner {
        private final Browser browser;
        private final Runner runner;

        InternalWebDriverRunner(Runner runner, Browser browser) {
            this.runner = runner;
            this.browser = browser;
        }

        @Override
        public Description getDescription() {
            return runner.getDescription();
        }

        @Override
        public void run(RunNotifier notifier) {
            currentThread().setName(browser.name() + "-" + currentThread().getName());

            try {
                WEB_DRIVER_CONTEXT.set(webDriverProvider.createDriverWith(browser));
            } catch (MalformedURLException e) {
                notifier.fireTestFailure(new Failure(getDescription(), e));
                return;
            }

            runner.run(notifier);

            WEB_DRIVER_CONTEXT.remove();
        }
    }
}
