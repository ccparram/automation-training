package tests;

import frameworks.web.WebDriverRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import tests.pages.SamplePage;

import static org.junit.Assert.assertNotNull;

@RunWith(WebDriverRunner.class)
public class SampleTest {

    private SamplePage samplePage;

    @Test
    public void test1() {
        assertNotNull("Instance not injected", samplePage);
        samplePage.navigate();
    }

    @Test
    public void test2() {
        assertNotNull("Instance not injected", samplePage);
        samplePage.navigate();
    }

    @Test
    public void test3() {
        assertNotNull("Instance not injected", samplePage);
        samplePage.navigate();
    }

    @Test
    public void test4() {
        assertNotNull("Instance not injected", samplePage);
        samplePage.navigate();
    }

    @Test
    public void test5() {
        assertNotNull("Instance not injected", samplePage);
        samplePage.navigate();
    }

}