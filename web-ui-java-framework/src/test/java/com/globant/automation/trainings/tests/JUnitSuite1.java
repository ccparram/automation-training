package com.globant.automation.trainings.tests;

import com.globant.automation.trainings.locale.UiText;
import com.globant.automation.trainings.pages.GoogleHomePage;
import com.globant.automation.trainings.pages.SamplePage;
import com.globant.automation.trainings.webdriver.tests.PomInjectorRule;
import com.globant.automation.trainings.webdriver.tests.WebDriverRunner;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(WebDriverRunner.Parallel.class)
public class JUnitSuite1 {

    @Rule
    public PomInjectorRule pomInjector = new PomInjectorRule();

    private SamplePage samplePage;

    private GoogleHomePage home;

    @Test
    public void test1() {
        assertNotNull("Instance not injected", samplePage);
        samplePage.open();
        assertThat("Sample page was not visible", samplePage.isVisible(), is(true));
    }

    @Test
    public void aWebDriverTest() throws Exception {
        assertNotNull("Instance not injected", home);
        home.open();
        List<String> results = home.search(UiText.Constants.SOMETHING).getResultsTexts();
        assertThat("No results links found", results.size(), is(greaterThan(0)));
    }

    private Matcher<Integer> greaterThan(final int i) {
        return new BaseMatcher<Integer>() {
            @Override
            public void describeTo(Description description) {

            }

            @Override
            public boolean matches(Object o) {
                return ((Integer) o) > i;
            }
        };
    }

}
