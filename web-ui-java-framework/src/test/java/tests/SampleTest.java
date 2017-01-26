package tests;

import frameworks.web.WebDriverRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import tests.locale.UiText;
import tests.pages.GoogleHomePage;
import tests.pages.SamplePage;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(WebDriverRunner.class)
public class SampleTest {

    private SamplePage samplePage;

    private GoogleHomePage home;

    @Test
    public void test1() {
        assertNotNull("Instance not injected", samplePage);
        samplePage.open();
        assertThat(samplePage.isVisible(), is(true));
    }

    @Test
    public void aWebDriverTest() throws Exception {
        assertNotNull("Instance not injected", home);
        home.open();
        home.search(UiText.Constants.SOMETHING).getResultsTexts();
    }

}
