package com.globant.automation.trainings.tests;

import com.globant.automation.trainings.pages.WikipediaArticle;
import com.globant.automation.trainings.pages.WikipediaHome;
import com.globant.automation.trainings.webdriver.tests.AbstractUIBaseTest;
import com.globant.automation.trainings.webdriver.tests.junit.WebDriverRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;

@RunWith(WebDriverRunner.Parallel.class)
public class WikipediaTest extends AbstractUIBaseTest {

    private WikipediaHome home;

    @Test
    public void homeIsVisible() {
        checkThat("Instance POM is injected", home, is(not(nullValue())));
        checkThat("Page Home is visible", home.isVisible(), is(true));
    }

    @Test
    public void searchArticleWithLanguage() throws Exception {
        checkThat("Instance POM is injected", home, is(not(nullValue())));
        WikipediaArticle results = home.withLanguage(TestContext.get().getLanguage()).doSearch("Anime");
        checkThat("Page Article is visible", results.isVisible(), is(true));
    }

}
