package com.globant.automation.trainings.frameworks.webdriver;

import com.globant.automation.trainings.frameworks.webdriver.tests.JUnit4WebDriverTest;
import org.junit.Test;

/**
 * Sample WebDriver test using the sample framework
 *
 * Refer to config.yml file in test/resources for settings
 *
 * @author Juan Krzemien
 */
public class WebDriverTests extends JUnit4WebDriverTest<GoogleHomePage> {

    @Test
    public void aWebDriverTest() throws Exception {
        GoogleHomePage home = new GoogleHomePage();
        home.search("Something...").getResultsTexts();
    }

}

