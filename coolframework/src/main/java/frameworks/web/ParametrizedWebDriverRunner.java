package frameworks.web;

import frameworks.config.Framework;
import frameworks.logging.Logging;
import frameworks.runner.ParametrizedParallelism;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParametrizedWebDriverRunner extends ParametrizedParallelism implements Logging {

    public ParametrizedWebDriverRunner(Class<?> clazz) throws Throwable {
        super(clazz);
        Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown));
    }

    @Override
    protected List<Runner> getChildren() {
        final List<Runner> runners = super.getChildren();
        final Set<Browser> browsers = Framework.CONFIGURATION.AvailableDrivers();
        final List<Runner> expandedRunners = new ArrayList<>(runners.size() * browsers.size());
        runners.forEach(r -> browsers.forEach(b -> expandedRunners.add(new InternalWebDriverRunner(r, b))));
        return expandedRunners;
    }

    @Override
    public Description getDescription() {
        return super.getDescription();
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
            ((BlockJUnit4ClassRunnerWithParametersInjector) runner).setBrowser(browser);
            runner.run(notifier);
        }

    }
}
