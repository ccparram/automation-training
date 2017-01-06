package tests;

import frameworks.tests.BaseTest;
import org.junit.Test;
import tests.pages.SamplePage;

import static org.junit.Assert.assertNotNull;

public class SampleTest extends BaseTest<SamplePage> {

    @Test
    public void test1() {
        assertNotNull("Instance not injected", getInitialPage());
        getInitialPage().navigate();
    }

    @Test
    public void test2() {
        assertNotNull("Instance not injected", getInitialPage());
        getInitialPage().navigate();
    }

    @Test
    public void test3() {
        assertNotNull("Instance not injected", getInitialPage());
        getInitialPage().navigate();
    }

    @Test
    public void test4() {
        assertNotNull("Instance not injected", getInitialPage());
        getInitialPage().navigate();
    }

    @Test
    public void test5() {
        assertNotNull("Instance not injected", getInitialPage());
        getInitialPage().navigate();
    }

}
