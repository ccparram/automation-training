package com.globant.automation.trainings.webdriver.tests.junit;

import com.globant.automation.trainings.languages.Language;
import com.globant.automation.trainings.runner.TestContext;
import com.globant.automation.trainings.runner.junit.ThreadPoolScheduler;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.tests.UIContext;
import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.TestWithParameters;

import java.util.List;

import static com.globant.automation.trainings.logging.Reporter.REPORTER;
import static com.globant.automation.trainings.webdriver.tests.junit.WebDriverRunner.multiplyTestMethodByBrowsersAndLanguages;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;

/**
 * @author Juan Krzemien
 */
class RunnerWithParametersInjector extends BlockJUnit4ClassRunnerWithParameters {

    RunnerWithParametersInjector(TestWithParameters test) throws InitializationError {
        super(test);
        setScheduler(new ThreadPoolScheduler());
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        return multiplyTestMethodByBrowsersAndLanguages(super.getChildren(), getTestClass());
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
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