package com.globant.automation.trainings.webdriver.tests.junit;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.tests.PageCommon;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.reflect.Field;

import static com.globant.automation.trainings.utils.Reflection.*;

/**
 * JUnit rule to inject POMs in test suites automatically
 *
 * @author Juan Krzemien
 */
public class PomInjectorRule implements MethodRule, Logging {

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                getFieldsFilteringBy(target, this::isPom).forEach(f -> injectFieldOwnInstance(f, target));
                base.evaluate();
            }

            private boolean isPom(Field field) {
                return fieldIsSubClassOf(field, PageCommon.class);
            }

        };
    }
}