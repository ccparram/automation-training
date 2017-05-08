package com.globant.automation.trainings.tests.junit;

import com.globant.automation.trainings.tests.Context;
import com.globant.automation.trainings.tests.TestContext;
import org.junit.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static com.globant.automation.trainings.logging.Reporter.REPORTER;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

/**
 * @author Juan Krzemien
 */
public class ExtentReportRule extends TestWatcher {

    protected String testFromDescription(Description description) {
        Context context = TestContext.get();
        if (context == null) {
            throw new IllegalStateException("Test context is null!");
        }

        return format("%s -> %s - %s", description.getTestClass().getSimpleName(), description.getMethodName(), context.getLanguage());
    }

    @Override
    protected void finished(Description description) {
        REPORTER.endTest();
    }

    @Override
    protected void failed(Throwable e, Description description) {
        String cause = e.getLocalizedMessage();
        if (cause == null || cause.isEmpty()) {
            if (e.getCause() != null) {
                cause = e.getCause().getLocalizedMessage();
            } else {
                cause = stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(joining("\\n"));
            }
        }
        REPORTER.fail("%s", cause);
    }

    @Override
    protected void starting(Description description) {
        REPORTER.startTest(testFromDescription(description), description.getMethodName());
    }

    @Override
    protected void skipped(AssumptionViolatedException e, Description description) {
        REPORTER.skip(testFromDescription(description) + " skipped");
    }

}
