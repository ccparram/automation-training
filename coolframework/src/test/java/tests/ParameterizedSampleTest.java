package tests;

import frameworks.runner.InjectPageObjectsRunnerFactory;
import frameworks.web.ParametrizedWebDriverRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;
import tests.pages.SamplePage;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

@RunWith(ParametrizedWebDriverRunner.class)
@UseParametersRunnerFactory(value = InjectPageObjectsRunnerFactory.class)
public class ParameterizedSampleTest {

    @Parameter
    public String sampleParam;

    private SamplePage samplePage;

    @Parameters
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
