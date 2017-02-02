package crap;

import com.globant.automation.trainings.webdriver.browsers.Browser;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * @author Juan Krzemien
 */
public class TestNGTest extends BaseTestNG {

    @Factory(dataProvider = "browsers")
    TestNGTest(Browser browser) {
        super(browser);
    }

    @Test
    public void aSampleTest() throws Exception {
        getLogger().info("Current browser is " + getBrowser());
    }

}
