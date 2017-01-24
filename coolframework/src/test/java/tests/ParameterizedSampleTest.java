package tests;

import frameworks.web.ParameterizedWebDriverTest;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import tests.pages.SamplePage;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

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
        samplePage.navigate();
    }

    @Test
    public void test2() {
        assertNotNull("Parameter not injected", sampleParam);
        assertNotNull("Instance not injected", samplePage);
        samplePage.navigate();
    }

    @Test
    public void test3() {
        assertNotNull("Parameter not injected", sampleParam);
        assertNotNull("Instance not injected", samplePage);
        samplePage.navigate();
    }

    @Test
    public void test4() {
        assertNotNull("Parameter not injected", sampleParam);
        assertNotNull("Instance not injected", samplePage);
        samplePage.navigate();
    }

    @Test
    public void test5() {
        assertNotNull("Parameter not injected", sampleParam);
        assertNotNull("Instance not injected", samplePage);
        samplePage.navigate();
    }

}
