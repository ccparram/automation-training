package com.globant.automation.trainings.webdriver.tests.junit;

import com.globant.automation.trainings.runner.ThreadPoolScheduler;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.languages.Language;
import com.globant.automation.trainings.webdriver.tests.TestContext;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.TestWithParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.globant.automation.trainings.webdriver.config.Framework.CONFIGURATION;
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

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        final Browser browser = ((WebDriverFrameworkMethod) method).getBrowser();
        final Language language = ((WebDriverFrameworkMethod) method).getLanguage();

        final String testName = format("%s-%s-%s", method.getName(), browser.name(), language.name());

        currentThread().setName(testName);

        try {
            TestContext.set(TestContext.with(browser, language));
            super.runChild(method, notifier);
        } catch (Exception e) {
            notifier.fireTestFailure(new Failure(getDescription(), e));
        } finally {
            TestContext.remove();
        }

    }
}