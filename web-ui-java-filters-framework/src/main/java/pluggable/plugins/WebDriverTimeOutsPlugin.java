package pluggable.plugins;


import org.openqa.selenium.WebDriver;
import pluggable.plugin.AbstractPlugin;

import static com.globant.automation.trainings.webdriver.config.Framework.CONFIGURATION;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Juan Krzemien
 */
public class WebDriverTimeOutsPlugin extends AbstractPlugin {

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    @Override
    public Object execute(Object input) {
        if (input instanceof WebDriver) {
            getLogger().info(format("Executing %s plugin...", getClass().getName()));
            WebDriver driver = (WebDriver) input;
            getLogger().info("Setting up WebDriver timeouts...");
            driver.manage().timeouts().implicitlyWait(CONFIGURATION.WebDriver().getImplicitTimeOut(), SECONDS);
            driver.manage().timeouts().pageLoadTimeout(CONFIGURATION.WebDriver().getPageLoadTimeout(), SECONDS);
            driver.manage().timeouts().setScriptTimeout(CONFIGURATION.WebDriver().getScriptTimeout(), SECONDS);
            return driver;
        }
        return input;
    }
}
