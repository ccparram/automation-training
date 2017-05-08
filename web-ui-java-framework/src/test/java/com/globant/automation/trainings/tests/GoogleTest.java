package com.globant.automation.trainings.tests;

import com.globant.automation.trainings.pages.GoogleHome;
import com.globant.automation.trainings.texts.Texts;
import com.globant.automation.trainings.webdriver.tests.junit.ParameterizedWebDriverTest;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;

public class GoogleTest extends ParameterizedWebDriverTest {

    @Parameter
    public String sampleParam;

    private GoogleHome home;

    @Parameters(name = "Parameter {0}")
    public static Iterable<Object> parameters() {
        return asList("Something to", "look forward");
    }

    @Test
    public void searchFromDictionary() throws Exception {
        checkThat("Instance POM is injected", home, is(not(nullValue())));
        List<String> results = home.search(Texts.Sample_Multi_Locale_Text()).getResultsTexts();
        checkThat("There are result links", results.size(), is(greaterThan(0)));
    }

    @Test
    public void searchFromParameter() throws Exception {
        checkThat("Parameter is injected", sampleParam, is(not(nullValue())));
        checkThat("Instance POM is injected", home, is(not(nullValue())));
        List<String> results = home.search(sampleParam).getResultsTexts();
        checkThat("There are result links", results.size(), is(greaterThan(0)));
    }

    private Matcher<Integer> greaterThan(final Integer i) {
        return new BaseMatcher<Integer>() {
            @Override
            public void describeTo(Description description) {
                description.appendText(format("greater than %d", i));
            }

            @Override
            public boolean matches(Object o) {
                return ((Integer) o) > i;
            }
        };
    }
}
