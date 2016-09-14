package com.globant.automation.trainings.webframework;

import com.globant.automation.trainings.webframework.pages.GoogleHomePage;
import com.globant.automation.trainings.webframework.tests.JUnit4WebDriverTest;
import com.globant.automation.trainings.webframework.utils.UiText;
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
