package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.logging.Logging;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import static com.globant.automation.trainings.logging.Reporter.REPORTER;


/**
 * JUnit rule to inject POMs in test suites automatically
 *
 * @author Juan Krzemien
 */
public class ScreenShotCaptureRule implements MethodRule, Logging {

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } catch (Exception e) {
                    REPORTER.addScreenShot(UIContext.captureScreenShot());
                    throw e; // rethrow to allow the failure to be reported to JUnit
                }
            }
        };
    }
}
