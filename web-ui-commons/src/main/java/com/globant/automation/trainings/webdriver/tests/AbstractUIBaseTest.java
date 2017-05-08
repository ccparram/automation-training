package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.tests.junit.BrowserAwareExtentReportRule;
import com.globant.automation.trainings.webdriver.tests.junit.CustomErrorCollector;
import com.globant.automation.trainings.webdriver.tests.junit.PomInjectorRule;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.openqa.selenium.Dimension;

import static com.globant.automation.trainings.logging.Reporter.REPORTER;
import static org.apache.commons.lang3.StringUtils.uncapitalize;


/**
 * @author Juan Krzemien
 */
public abstract class AbstractUIBaseTest implements Logging {

    protected static final Dimension RESPONSIVE_DIMENSIONS = new Dimension(500, 800);

    @Rule
    public CustomErrorCollector errors = new CustomErrorCollector();

    @Rule
    public PomInjectorRule pomInjector = new PomInjectorRule();

    @Rule
    public BrowserAwareExtentReportRule reporter = new BrowserAwareExtentReportRule();

    @Rule
    public ScreenShotCaptureRule screenShotCaptureRule = new ScreenShotCaptureRule();

    protected <T> void checkThat(String validation, T actual, Matcher<T> expected) {
        REPORTER.info("Verifying that " + uncapitalize(validation));
        errors.checkThat(validation, actual, expected);
    }

}
