package com.globant.automation.trainings.frameworks.webdriver;

import com.globant.automation.trainings.frameworks.webdriver.pages.GoogleHomePage;
import com.globant.automation.trainings.frameworks.webdriver.tests.JUnit4WebDriverTest;
import com.globant.automation.trainings.frameworks.webdriver.tests.language.AbstractUiText;
import org.junit.Test;

/**
 * Sample WebDriver test using the sample framework
 * <p>
 * Refer to config.yml file in test/resources for settings
 *
 * @author Juan Krzemien
 */
public class WebDriverTests extends JUnit4WebDriverTest<GoogleHomePage> {

    @Test
    public void aWebDriverTest() throws Exception {
        GoogleHomePage home = new GoogleHomePage();
        home.search(UiText.Constants.SOMETHING.toString()).getResultsTexts();
    }

}

class UiText extends AbstractUiText {

    UiText() {
    }

    public enum Constants {

        SOMETHING;

        @Override
        public String toString() {
            return getFor(name());
        }
    }

}

