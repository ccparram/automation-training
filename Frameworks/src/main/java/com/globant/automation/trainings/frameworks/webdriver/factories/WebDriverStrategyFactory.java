package com.globant.automation.trainings.frameworks.webdriver.factories;

import com.globant.automation.trainings.frameworks.webdriver.enums.Browser;
import com.globant.automation.trainings.frameworks.webdriver.listeners.BasicWebDriverListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.globant.automation.trainings.frameworks.webdriver.config.Framework.CONFIGURATION;
import static java.lang.Boolean.parseBoolean;
import static java.lang.System.getProperty;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.remote.CapabilityType.PROXY;

/**
 * @author Juan Krzemien
 */
class WebDriverStrategyFactory {

    private final boolean debugMode = parseBoolean(getProperty("DEBUG_MODE", "false"));

    WebDriver getDriverFor(Browser browser) {

        DesiredCapabilities capabilities = new DesiredCapabilities(browser.getCapabilities());

        Map<String, Object> driverConfig = CONFIGURATION.Driver(browser).getCapabilities();

        capabilities.merge(new DesiredCapabilities(driverConfig));

        // Set proxy settings, if any
        if (CONFIGURATION.Proxy().isEnabled()) {
            capabilities.setCapability(PROXY, CONFIGURATION.Proxy().getHost() + ":" + CONFIGURATION.Proxy().getPort());
        }

        if (!debugMode) {
            Logger logger = Logger.getLogger("");
            logger.setLevel(Level.OFF);
        }

        return getDriverFor(capabilities);
    }

    WebDriver getDriverFor(DesiredCapabilities capabilities) {
        // Create driver
        WebDriver driver = new RemoteWebDriver(CONFIGURATION.WebDriver().getRemoteURL(), capabilities);

        driver.manage().timeouts().implicitlyWait(CONFIGURATION.WebDriver().getImplicitTimeOut(), SECONDS);
        driver.manage().timeouts().pageLoadTimeout(CONFIGURATION.WebDriver().getPageLoadTimeout(), SECONDS);
        driver.manage().timeouts().setScriptTimeout(CONFIGURATION.WebDriver().getScriptTimeout(), SECONDS);

        driver.manage().window().maximize();

        if (CONFIGURATION.WebDriver().isUseListener()) {
            // Wrap the driver as an event firing one, and add basic listener
            final EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
            eventDriver.register(new BasicWebDriverListener());
            return eventDriver;
        }

        return driver;
    }

}
