package com.globant.automation.trainings.webdriver.tests.junit;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.tests.UIContext;
import org.junit.rules.ErrorCollector;

import java.io.IOException;

import static com.globant.automation.trainings.logging.Reporter.REPORTER;
import static org.apache.commons.lang3.StringUtils.capitalize;

/**
 * @author Juan Krzemien
 */
public class CustomErrorCollector extends ErrorCollector implements Logging {

    @Override
    public void addError(Throwable error) {
        REPORTER.fail(capitalize(error.getLocalizedMessage()));
        try {
            REPORTER.addScreenShot(UIContext.captureScreenShot());
        } catch (IOException e) {
            getLogger().error(e.getLocalizedMessage(), e);
        }
        super.addError(error);
    }

}
