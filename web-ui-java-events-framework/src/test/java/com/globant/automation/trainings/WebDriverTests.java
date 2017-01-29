package com.globant.automation.trainings;

import com.globant.automation.trainings.pages.GoogleHomePage;
import com.globant.automation.trainings.tests.JUnit4WebDriverTest;
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
        home.search("Something something dark side...").getResultsTexts();
    }

}
