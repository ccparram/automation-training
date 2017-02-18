package com.globant.automation.trainings.tests;

import com.globant.automation.trainings.pages.SamplePage;
import com.globant.automation.trainings.webdriver.tests.junit.ExtendedReporterRule;
import com.globant.automation.trainings.webdriver.tests.junit.PomInjectorRule;
import com.globant.automation.trainings.webdriver.tests.junit.WebDriverRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(WebDriverRunner.Parallel.class)
public class JUnitSuite1 {

    @Rule
    public PomInjectorRule pomInjector = new PomInjectorRule();

    @Rule
    public ExtendedReporterRule extendedReportListener = new ExtendedReporterRule();

    private SamplePage samplePage;

    @Test
    public void test1() {
        assertNotNull("Instance not injected", samplePage);
        assertThat("Sample page was not visible", samplePage.isVisible(), is(true));
    }

    @Test
    public void aWebDriverTest() throws Exception {
        assertNotNull("Instance not injected", samplePage);
        SamplePage results = samplePage.doSearch("Evangelion");
        assertThat("Sample page was not visible", results.isVisible(), is(true));
    }

}
