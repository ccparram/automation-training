package tests;

import frameworks.annotations.PageObject;
import frameworks.runner.AutomationFramework;
import org.junit.Test;
import org.junit.runner.RunWith;
import tests.pages.SamplePage;

import static org.junit.Assert.assertNotNull;

/**
 * @author Juan Krzemien
 */

@RunWith(AutomationFramework.class)
public class SampleTest {

    @PageObject
    SamplePage samplePage;

    @Test
    public void test1() {
        assertNotNull("Instance not injected", samplePage);
    }
}
