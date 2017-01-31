package pluggable.plugins;


import com.globant.automation.trainings.webdriver.listeners.BasicWebDriverListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import pluggable.plugin.AbstractPlugin;

import static com.globant.automation.trainings.webdriver.config.Framework.CONFIGURATION;
import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public class WebDriverBasicListenerPlugin extends AbstractPlugin {

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }

    @Override
    public Object execute(Object input) {
        if (input instanceof WebDriver && CONFIGURATION.WebDriver().isUseListener()) {
            getLogger().info(format("Executing %s plugin...", getClass().getName()));
            WebDriver driver = (WebDriver) input;
            getLogger().info("Setting up WebDriver listener...");
            // Wrap the driver as an event firing one, and add basic listener
            final EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
            eventDriver.register(new BasicWebDriverListener());
            return eventDriver;
        }
        return input;
    }
}
