package com.globant.automation.trainings.tests.junit;

import com.globant.automation.trainings.annotations.IgnoreLanguages;
import com.globant.automation.trainings.languages.Language;
import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.tests.DefaultContext;
import com.globant.automation.trainings.tests.TestContext;
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

import static com.globant.automation.trainings.config.CommonSettings.COMMON;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;
import static org.junit.runner.Description.createTestDescription;

/**
 * JUnit runner for parallel multi language tests execution.
 *
 * @author Juan Krzemien
 */

public class LanguagesRunner implements Logging {

    private LanguagesRunner() {
    }

    public static class ParametrizedParallel extends Parameterized {
        public ParametrizedParallel(Class<?> klass) throws Throwable {
            super(klass);
            setScheduler(new ThreadPoolScheduler());
        }
    }

    public static class ParameterizedSingleThread extends Parameterized {
        public ParameterizedSingleThread(Class<?> klass) throws Throwable {
            super(klass);
        }
    }

    public static class Parallel extends LanguageAwareRunner {
        public Parallel(Class<?> klass) throws InitializationError {
            super(klass);
            setScheduler(new ThreadPoolScheduler());
        }
    }

    public static class SingleThread extends LanguageAwareRunner {
        public SingleThread(Class<?> klass) throws InitializationError {
            super(klass);
        }
    }

    static class LanguageAwareRunner extends BlockJUnit4ClassRunner {

        /**
         * Creates a BlockJUnit4ClassRunner to run {@code klass}
         *
         * @param klass A JUnit test class
         * @throws InitializationError if the test class is malformed.
         */
        LanguageAwareRunner(Class<?> klass) throws InitializationError {
            super(klass);
        }

        @Override
        public Description getDescription() {
            return Description.EMPTY;
        }

        @Override
        protected List<FrameworkMethod> getChildren() {
            return multiplyTestMethodByLanguages();
        }

        @Override
        protected void runChild(FrameworkMethod method, RunNotifier notifier) {
            runApiTestMethod(method, notifier);
        }

        List<FrameworkMethod> multiplyTestMethodByLanguages() {
            final List<FrameworkMethod> methods = super.getChildren();
            final Set<Language> languages = COMMON.availableLanguages();
            final List<FrameworkMethod> expandedMethods = new ArrayList<>(methods.size() * languages.size());
            IgnoreLanguages ignoreInClass = getTestClass().getJavaClass().getAnnotation(IgnoreLanguages.class);
            methods.forEach(m ->
                    languages.forEach(l -> {
                        IgnoreLanguages toIgnore = ignoreInClass;
                        if (toIgnore == null) {
                            toIgnore = m.getAnnotation(IgnoreLanguages.class);
                        }
                        if (toIgnore == null || stream(toIgnore.value()).noneMatch(ignore -> ignore.equals(l))) {
                            expandedMethods.add(new ApiFrameworkMethod(m, l));
                        }
                    })
            );
            return expandedMethods;
        }

        void runApiTestMethod(FrameworkMethod method, RunNotifier notifier) {
            final Language language = ((ApiFrameworkMethod) method).getLanguage();

            final String testName = format("%s-%s", method.getName(), language.name());

            currentThread().setName(testName);

            try {
                TestContext.set(new DefaultContext(language));
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
