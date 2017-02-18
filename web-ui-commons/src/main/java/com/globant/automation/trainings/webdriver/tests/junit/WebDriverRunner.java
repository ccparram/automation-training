package com.globant.automation.trainings.webdriver.tests.junit;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.runner.ThreadPoolScheduler;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.languages.Language;
import com.globant.automation.trainings.webdriver.server.SeleniumServerStandAlone;
import com.globant.automation.trainings.webdriver.tests.TestContext;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Parameterized;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.globant.automation.trainings.webdriver.config.Framework.CONFIGURATION;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static org.junit.runner.Description.createTestDescription;

/**
 * JUnit runner for parallel OR single-thread WebDriver based tests executions.
 *
 * @author Juan Krzemien
 */
public class WebDriverRunner implements Logging {

    public static class ParametrizedParallel extends Parameterized {
        public ParametrizedParallel(Class<?> clazz) throws Throwable {
            super(clazz);
            setScheduler(new ThreadPoolScheduler());
        }
    }

    public static class Parallel extends WebDriverAwareParallelRunner {

        public Parallel(Class<?> clazz) throws Throwable {
            super(clazz);
        }

        @Override
        public Description getDescription() {
            return Description.EMPTY;
        }

        @Override
        protected List<FrameworkMethod> getChildren() {
            return multiplyTestMethodByBrowsersAndLanguages();
        }

        @Override
        protected void runChild(FrameworkMethod method, RunNotifier notifier) {
            runWebDriverTestMethod(method, notifier);
        }

    }

    public static class SingleThread extends WebDriverAwareRunner {

        public SingleThread(Class<?> clazz) throws Throwable {
            super(clazz);
        }

        @Override
        public Description getDescription() {
            return Description.EMPTY;
        }

        @Override
        protected List<FrameworkMethod> getChildren() {
            return multiplyTestMethodByBrowsersAndLanguages();
        }

        @Override
        protected void runChild(FrameworkMethod method, RunNotifier notifier) {
            runWebDriverTestMethod(method, notifier);
        }

    }

    static class WebDriverAwareParallelRunner extends WebDriverAwareRunner {
        /**
         * Creates a BlockJUnit4ClassRunner to run {@code klass}
         *
         * @param klass JUnit class
         * @throws InitializationError if the test class is malformed.
         */
        WebDriverAwareParallelRunner(Class<?> klass) throws InitializationError {
            super(klass);
            setScheduler(new ThreadPoolScheduler());
        }
    }

    static class WebDriverAwareRunner extends BlockJUnit4ClassRunner {

        /**
         * Creates a BlockJUnit4ClassRunner to run {@code klass}
         *
         * @param klass A JUnit test class
         * @throws InitializationError if the test class is malformed.
         */
        WebDriverAwareRunner(Class<?> klass) throws InitializationError {
            super(klass);
            if (!CONFIGURATION.WebDriver().isSeleniumGrid()) {
                Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown));
            }
        }

        List<FrameworkMethod> multiplyTestMethodByBrowsersAndLanguages() {
            final List<FrameworkMethod> methods = super.getChildren();
            final Set<Browser> browsers = CONFIGURATION.AvailableDrivers();
            final Set<Language> languages = CONFIGURATION.AvailableLanguages();
            final List<FrameworkMethod> expandedMethods = new ArrayList<>(methods.size() * browsers.size() * languages.size());
            methods.forEach(m ->
                    browsers.forEach(b ->
                            languages.forEach(l -> expandedMethods.add(new WebDriverFrameworkMethod(m, b, l))
                            )
                    )
            );
            return expandedMethods;
        }

        void runWebDriverTestMethod(FrameworkMethod method, RunNotifier notifier) {
            final Browser browser = ((WebDriverFrameworkMethod) method).getBrowser();
            final Language language = ((WebDriverFrameworkMethod) method).getLanguage();

            final String testName = format("%s-%s-%s", method.getName(), browser.name(), language.name());

            currentThread().setName(testName);

            try {
                TestContext.set(TestContext.with(browser, language));
                final Description description = createTestDescription(getTestClass().getJavaClass(), testName);
                runLeaf(methodBlock(method), description, notifier);
            } catch (Exception e) {
                notifier.fireTestFailure(new Failure(getDescription(), e));
            } finally {
                TestContext.remove();
            }
        }
    }
}
