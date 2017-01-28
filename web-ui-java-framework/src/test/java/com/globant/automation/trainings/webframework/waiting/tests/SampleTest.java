package com.globant.automation.trainings.webframework.waiting.tests;

import com.globant.automation.trainings.webframework.WebDriverRunner;
import com.globant.automation.trainings.webframework.waiting.tests.pages.GoogleHomePage;
import com.globant.automation.trainings.webframework.waiting.tests.pages.SamplePage;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.globant.automation.trainings.webframework.waiting.tests.locale.UiText;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(WebDriverRunner.Parallel.class)
public class SampleTest {

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
