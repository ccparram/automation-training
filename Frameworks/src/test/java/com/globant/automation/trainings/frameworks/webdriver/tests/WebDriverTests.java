package com.globant.automation.trainings.frameworks.webdriver.tests;

import com.globant.automation.trainings.frameworks.webdriver.pages.GoogleHomePage;
import com.globant.automation.trainings.frameworks.webdriver.test.JUnit4WebDriverTest;
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

