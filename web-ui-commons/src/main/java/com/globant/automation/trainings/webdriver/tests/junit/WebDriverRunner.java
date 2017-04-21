package com.globant.automation.trainings.webdriver.tests.junit;

import com.globant.automation.trainings.annotations.IgnoreLanguages;
import com.globant.automation.trainings.languages.Language;
import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.runner.TestContext;
import com.globant.automation.trainings.runner.junit.ThreadPoolScheduler;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.server.SeleniumServerStandAlone;
import com.globant.automation.trainings.webdriver.tests.UIContext;
import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Parameterized;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.globant.automation.trainings.config.CommonSettings.COMMON;
import static com.globant.automation.trainings.logging.Reporter.REPORTER;
import static com.globant.automation.trainings.webdriver.config.UISettings.UI;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

/**
 * JUnit runners definitions for WebDriver based tests executions.
 *
 * @author Juan Krzemien
 */

public class WebDriverRunner implements Logging {

    private WebDriverRunner() {
    }

    private static void stopSeleniumServerOnJvmExit() {
        if (!UI.WebDriver().isSeleniumGrid()) {
            Runtime.getRuntime().addShutdownHook(new Thread(SeleniumServerStandAlone.INSTANCE::shutdown));
        }
    }

    static List<FrameworkMethod> multiplyTestMethodByBrowsersAndLanguages(List<FrameworkMethod> methods, TestClass testClass) {
        final Set<Browser> browsers = UI.AvailableDrivers();
        final Set<Language> languages = COMMON.availableLanguages();
        final List<FrameworkMethod> expandedMethods = new ArrayList<>(methods.size() * browsers.size() * languages.size());
        IgnoreLanguages ignoreInClass = testClass.getJavaClass().getAnnotation(IgnoreLanguages.class);
        methods.forEach(m ->
                browsers.forEach(b ->
                        languages.forEach(l -> {
                            IgnoreLanguages toIgnore = ignoreInClass;
                            if (toIgnore == null) {
                                toIgnore = m.getAnnotation(IgnoreLanguages.class);
                            }
                            if (toIgnore == null || stream(toIgnore.value()).noneMatch(ignore -> ignore.equals(l))) {
                                expandedMethods.add(new WebDriverFrameworkMethod(m, b, l));
                            }
                        })
                )
        );
        return expandedMethods;
    }

    public static class Parametrized extends Parameterized {
        public Parametrized(Class<?> klass) throws Throwable {
            super(klass);
            stopSeleniumServerOnJvmExit();
        }
    }

    public static class ParametrizedParallel extends Parameterized {
        public ParametrizedParallel(Class<?> klass) throws Throwable {
            super(klass);
            setScheduler(new ThreadPoolScheduler());
            stopSeleniumServerOnJvmExit();
        }
    }

    public static class Parallel extends WebDriverAwareRunner {

        /**
         * Creates a WebDriver aware parallel runner to run {@code klass}
         *
         * @param klass A JUnit test class
         * @throws InitializationError if the test class is malformed.
         */
        public Parallel(Class<?> klass) throws InitializationError {
            super(klass);
            setScheduler(new ThreadPoolScheduler());
        }

    }

    public static class SingleThread extends WebDriverAwareRunner {

        /**
         * Creates a WebDriver aware single threaded runner to run {@code klass}
         *
         * @param klass A JUnit test class
         * @throws InitializationError if the test class is malformed.
         */
        public SingleThread(Class<?> klass) throws InitializationError {
            super(klass);
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
            stopSeleniumServerOnJvmExit();
        }

        @Override
        public Description getDescription() {
            return Description.EMPTY;
        }

        @Override
        protected List<FrameworkMethod> getChildren() {
            return multiplyTestMethodByBrowsersAndLanguages(super.getChildren(), getTestClass());
        }

        @Override
        protected void runChild(FrameworkMethod method, RunNotifier notifier) {
            runWebDriverTestMethod(method, notifier);
        }

        private void runWebDriverTestMethod(FrameworkMethod method, RunNotifier notifier) {
            final Browser browser = ((WebDriverFrameworkMethod) method).getBrowser();
            final Language language = ((WebDriverFrameworkMethod) method).getLanguage();

            final String testName = format("%s-%s-%s", method.getName(), browser.name(), language.name());

            currentThread().setName(testName);

            try {
                TestContext.set(UIContext.with(browser, language));
                Ignore ignore = method.getAnnotation(Ignore.class);
                if (ignore != null) {
                    Description description = describeChild(method);
                    notifier.fireTestIgnored(description);
                    REPORTER.startTest(testName, format("REASON: %s", ignore.value()));
                    REPORTER.skip(format("Test [%s] is marked with @Ignore. Skipping.", testName));
                    REPORTER.endTest();
                    return;
                }
                super.runChild(method, notifier);
            } catch (Exception e) {
                notifier.fireTestFailure(new Failure(getDescription(), e));
            } finally {
                TestContext.remove();
            }
        }
    }
}
