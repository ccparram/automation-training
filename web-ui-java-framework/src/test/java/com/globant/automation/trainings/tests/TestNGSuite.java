package com.globant.automation.trainings.tests;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.pages.GoogleHomePage;
import com.globant.automation.trainings.pages.SamplePage2;
import com.globant.automation.trainings.webdriver.tests.WebDriverTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * @author Juan Krzemien
 */
@Listeners(WebDriverTestNGListener.class)
public class TestNGSuite implements Logging {

    SamplePage2 samplePage;
    GoogleHomePage home;

    @Test
    public void aSampleTest() throws Exception {
        getLogger().info("Current POM is " + samplePage.toString());
    }

    @Test
    public void aWebDriverTest() throws Exception {
        home.open();
        home.search("Something something dark side...").getResultsTexts();
    }

}
