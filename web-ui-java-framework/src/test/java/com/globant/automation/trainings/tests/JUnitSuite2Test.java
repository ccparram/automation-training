package com.globant.automation.trainings.tests;

import com.globant.automation.trainings.pages.GoogleHomePage;
import com.globant.automation.trainings.webdriver.tests.junit.ParameterizedWebDriverTest;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class JUnitSuite2Test extends ParameterizedWebDriverTest {

    @Parameter
    public String sampleParam;

    private GoogleHomePage home;

    @Parameters(name = "Parameter {0}")
    public static Iterable<Object> parameters() {
        return Arrays.asList("Something to", "look forward");
    }

    @Test
    public void test1() throws Exception {
        assertNotNull("Instance not injected", home);

        List<String> results = home
                .search("Something")
                .getResultsTexts();

        assertThat("No results links found", results.size(), is(greaterThan(0)));
    }

    @Test
    public void aWebDriverTest() throws Exception {
        assertNotNull("Parameter not injected", sampleParam);
        assertNotNull("Instance not injected", home);
        home.search(sampleParam).getResultsTexts();
        System.err.println(format("From inside test 2. Parameter is %s. Thread ID is %s and its name is %s", sampleParam, currentThread().getId(), currentThread().getName()));
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
