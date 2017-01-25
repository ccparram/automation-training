package tests;

import frameworks.web.ParameterizedWebDriverTest;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import tests.pages.SamplePage;

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

    @Parameters(name = "Parameter {0}")
    public static Iterable<Object> parameters() {
        return Arrays.asList("A", "B");
    }

    @Test
    public void test1() {
        assertNotNull("Parameter not injected", sampleParam);
        assertNotNull("Instance not injected", samplePage);
        assertThat(samplePage.isVisible(), is(true));
        System.err.println(format("From inside test 1. Parameter is %s. Thread ID is %s and its name is %s", sampleParam, currentThread().getId(), currentThread().getName()));
    }

    @Test
    public void test2() {
        assertNotNull("Parameter not injected", sampleParam);
        assertNotNull("Instance not injected", samplePage);
        assertThat(samplePage.isVisible(), is(true));
        System.err.println(format("From inside test 2. Parameter is %s. Thread ID is %s and its name is %s", sampleParam, currentThread().getId(), currentThread().getName()));
    }

    @Test
    public void test3() {
        assertNotNull("Parameter not injected", sampleParam);
        assertNotNull("Instance not injected", samplePage);
        assertThat(samplePage.isVisible(), is(true));
        System.err.println(format("From inside test 3. Parameter is %s. Thread ID is %s and its name is %s", sampleParam, currentThread().getId(), currentThread().getName()));
    }

    @Test
    public void test4() {
        assertNotNull("Parameter not injected", sampleParam);
        assertNotNull("Instance not injected", samplePage);
        assertThat(samplePage.isVisible(), is(true));
        System.err.println(format("From inside test 4. Parameter is %s. Thread ID is %s and its name is %s", sampleParam, currentThread().getId(), currentThread().getName()));
    }

    @Test
    public void test5() {
        assertNotNull("Parameter not injected", sampleParam);
        assertNotNull("Instance not injected", samplePage);
        assertThat(samplePage.isVisible(), is(true));
        System.err.println(format("From inside test 5. Parameter is %s. Thread ID is %s and its name is %s", sampleParam, currentThread().getId(), currentThread().getName()));
    }

}
