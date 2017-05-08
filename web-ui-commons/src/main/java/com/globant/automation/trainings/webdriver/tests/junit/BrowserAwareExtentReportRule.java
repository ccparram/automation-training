package com.globant.automation.trainings.webdriver.tests.junit;

import com.globant.automation.trainings.tests.TestContext;
import com.globant.automation.trainings.tests.junit.ExtentReportRule;
import com.globant.automation.trainings.webdriver.tests.UIContext;
import org.junit.runner.Description;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public class BrowserAwareExtentReportRule extends ExtentReportRule {

    @Override
    protected String testFromDescription(Description description) {
        UIContext context = (UIContext) TestContext.get();
        if (context == null) {
            throw new IllegalStateException("Test context is null!");
        }

        return format("%s -> %s - %s - %s", description.getTestClass().getSimpleName(), description.getMethodName(), context.getBrowser().name(), context.getLanguage());
    }

}
