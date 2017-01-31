package pluggable;

import com.globant.automation.trainings.logging.Logging;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

/**
 * @author Juan Krzemien
 */
public class PluginWatcherTests implements Logging {

    @Test
    public void aFolderWatcherTest() throws Exception {

        ChromeDriverManager.getInstance().setup();
        WebDriver driver = null;
        try (ObjectsContainer objectsContainer = ObjectsContainer.INSTANCE) {

            objectsContainer.add(new ChromeDriver());

            driver = objectsContainer.get(WebDriver.class).get(0);

            getLogger().info("value: " + driver.toString());
            getLogger().info("value: " + driver.manage().timeouts().toString());

            TimeUnit.SECONDS.sleep(1);

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
