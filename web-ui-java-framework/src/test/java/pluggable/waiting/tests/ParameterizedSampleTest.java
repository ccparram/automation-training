package pluggable.waiting.tests;

import com.globant.automation.trainings.ParameterizedWebDriverTest;
import pluggable.waiting.tests.pages.GoogleHomePage;
import pluggable.waiting.tests.pages.SamplePage;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ParameterizedSampleTest extends ParameterizedWebDriverTest {

    @Parameter
    public String sampleParam;

    private SamplePage samplePage;

    private GoogleHomePage home;

    @Parameters(name = "Parameter {0}")
    public static Iterable<Object> parameters() {
        return Arrays.asList("Something to", "look forward");
    }

    @Test
    public void test1() {
        assertNotNull("Parameter not injected", sampleParam);
        assertNotNull("Instance not injected", samplePage);
        samplePage.open();
        assertThat(samplePage.isVisible(), is(true));
        System.err.println(format("From inside test 1. Parameter is %s. Thread ID is %s and its name is %s", sampleParam, currentThread().getId(), currentThread().getName()));

    }

    @Test
    public void aWebDriverTest() throws Exception {
        assertNotNull("Parameter not injected", sampleParam);
        assertNotNull("Instance not injected", home);
        home.open();
        home.search(sampleParam).getResultsTexts();
        System.err.println(format("From inside test 2. Parameter is %s. Thread ID is %s and its name is %s", sampleParam, currentThread().getId(), currentThread().getName()));
    }

}
