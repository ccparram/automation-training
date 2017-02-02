package pluggable.plugins;


import org.openqa.selenium.WebDriver;
import pluggable.plugin.AbstractPlugin;
import pluggable.plugin.Priority;

import static com.globant.automation.trainings.webdriver.config.Framework.CONFIGURATION;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Juan Krzemien
 */
public class WebDriverTimeOutsPlugin extends AbstractPlugin<WebDriver> {

    @Override
    public WebDriver execute(WebDriver input) {
        getLogger().info("Setting up WebDriver timeouts...");
        input.manage().timeouts().implicitlyWait(CONFIGURATION.WebDriver().getImplicitTimeOut(), SECONDS);
        input.manage().timeouts().pageLoadTimeout(CONFIGURATION.WebDriver().getPageLoadTimeout(), SECONDS);
        input.manage().timeouts().setScriptTimeout(CONFIGURATION.WebDriver().getScriptTimeout(), SECONDS);
        return input;
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

}