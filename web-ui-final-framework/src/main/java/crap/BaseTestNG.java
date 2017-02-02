package crap;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.globant.automation.trainings.webdriver.config.Framework.CONFIGURATION;

/**
 * @author Juan Krzemien
 */
public abstract class BaseTestNG extends BothCrap implements Logging {

    private final Browser browser;
    private WebDriver driver;

    protected BaseTestNG(Browser browser) {
        this.browser = browser;
    }

    @DataProvider
    static Object[][] browsers() {
        Object[][] crap = new Object[CONFIGURATION.AvailableDrivers().size()][1];
        AtomicInteger i = new AtomicInteger();
        CONFIGURATION.AvailableDrivers().forEach(b -> crap[i.getAndIncrement()][0] = b);
        return crap;
    }

    Browser getBrowser() {
        return browser;
    }

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        driver = new WebDriverProvider().createDriverWith(browser);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
