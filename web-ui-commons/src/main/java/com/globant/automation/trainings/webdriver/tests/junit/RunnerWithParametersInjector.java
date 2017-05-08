package com.globant.automation.trainings.webdriver.tests.junit;

import com.globant.automation.trainings.annotations.IgnoreLanguages;
import com.globant.automation.trainings.languages.Language;
import com.globant.automation.trainings.tests.TestContext;
import com.globant.automation.trainings.tests.junit.ThreadPoolScheduler;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.tests.UIContext;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.TestWithParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.globant.automation.trainings.config.CommonSettings.COMMON;
import static com.globant.automation.trainings.webdriver.config.UISettings.UI;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

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
        final Set<Browser> browsers = UI.AvailableDrivers();
        final Set<Language> languages = COMMON.availableLanguages();
        final List<FrameworkMethod> expandedMethods = new ArrayList<>(methods.size() * browsers.size() * languages.size());
        IgnoreLanguages ignoreInClass = getTestClass().getJavaClass().getAnnotation(IgnoreLanguages.class);
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

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        final Browser browser = ((WebDriverFrameworkMethod) method).getBrowser();
        final Language language = ((WebDriverFrameworkMethod) method).getLanguage();

        final String testName = format("%s-%s-%s", method.getName(), browser.name(), language.name());

        currentThread().setName(testName);

        try {
            TestContext.set(UIContext.with(browser, language));
            super.runChild(method, notifier);
        } catch (Exception e) {
            notifier.fireTestFailure(new Failure(getDescription(), e));
        } finally {
            TestContext.remove();
        }

    }
}
