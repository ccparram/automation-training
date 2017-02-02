package crap;

import com.globant.automation.trainings.logging.Logging;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * @author Juan Krzemien
 */
@Listeners(WebDriverTestNGListener.class)
public class TestNGTest implements Logging { //extends BaseTestNG {

    SamplePage samplePage;

    @Test
    public void aSampleTest() throws Exception {
        getLogger().info("Current POM is " + samplePage.toString());
    }

}
