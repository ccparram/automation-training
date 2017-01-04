package tests;

import frameworks.annotations.PageObject;
import frameworks.runner.AutomationFramework;
import frameworks.runner.ContainerAwareRunnerFactory;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;
import tests.pages.SamplePage;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

/**
 * @author Juan Krzemien
 */

@RunWith(AutomationFramework.class)
@UseParametersRunnerFactory(ContainerAwareRunnerFactory.class)
public class SampleTest {

    @PageObject
    SamplePage samplePage;

    @Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {},
                {},
                {},
                {},
                {},
                {}
        });
    }

    @Test
    public void test1() {
        assertNotNull("Instance not injected", samplePage);
    }

    @After
    public void tearDown() {
        samplePage.dispose();
    }
}
