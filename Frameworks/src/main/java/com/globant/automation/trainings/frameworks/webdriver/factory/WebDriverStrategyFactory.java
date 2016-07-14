package com.globant.automation.trainings.frameworks.webdriver.factory;

import com.globant.automation.trainings.frameworks.webdriver.enums.Browser;
import com.globant.automation.trainings.frameworks.webdriver.listeners.BasicWebDriverListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.Map;

import static com.globant.automation.trainings.frameworks.webdriver.config.Framework.CONFIGURATION;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.remote.CapabilityType.PROXY;

/**
 * @author Juan Krzemien
 */
public class WebDriverStrategyFactory {

    public WebDriver getDriverFor(Browser browser) {

        DesiredCapabilities capabilities = new DesiredCapabilities(browser.getCapabilities());

        Map<String, Object> driverConfig = CONFIGURATION.Driver(browser).getCapabilities();

        capabilities.merge(new DesiredCapabilities(driverConfig));

        // Set proxy settings, if any
        if (CONFIGURATION.Proxy().isEnabled()) {
            capabilities.setCapability(PROXY, CONFIGURATION.Proxy().getHost() + ":" + CONFIGURATION.Proxy().getPort());
        }

        return getDriverFor(capabilities);
    }

    public WebDriver getDriverFor(DesiredCapabilities capabilities) {
        // Create driver
        WebDriver driver = new RemoteWebDriver(capabilities);

        driver.manage().timeouts().implicitlyWait(CONFIGURATION.WebDriver().getImplicitTimeOut(), SECONDS);
        driver.manage().timeouts().pageLoadTimeout(CONFIGURATION.WebDriver().getPageLoadTimeout(), SECONDS);
        driver.manage().timeouts().setScriptTimeout(CONFIGURATION.WebDriver().getScriptTimeout(), SECONDS);

        if (CONFIGURATION.WebDriver().isUseListener()) {
            // Wrap the driver as an event firing one, and add basic listener
            final EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
            eventDriver.register(new BasicWebDriverListener());
            return eventDriver;
        }

        return driver;
    }

}
