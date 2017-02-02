package pluggable.plugins;


import com.globant.automation.trainings.webdriver.listeners.BasicWebDriverListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import pluggable.plugin.AbstractPlugin;
import pluggable.plugin.Priority;

import static pluggable.plugin.Priority.LOW;

/**
 * @author Juan Krzemien
 */
public class WebDriverBasicListenerPlugin extends AbstractPlugin<WebDriver> {

    @Override
    public WebDriver execute(WebDriver input) {
        getLogger().info("Executing plugin...");
        getLogger().info("Setting up WebDriver listener...");
        // Wrap the driver as an event firing one, and add basic listener
        final EventFiringWebDriver eventDriver = new EventFiringWebDriver(input);
        eventDriver.register(new BasicWebDriverListener());
        return eventDriver;
    }

    @Override
    public Priority getPriority() {
        return LOW;
    }
}
