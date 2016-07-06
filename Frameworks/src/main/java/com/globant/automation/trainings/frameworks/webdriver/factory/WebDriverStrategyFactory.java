package com.globant.automation.trainings.frameworks.webdriver.factory;

import com.globant.automation.trainings.frameworks.webdriver.enums.Browser;
import com.globant.automation.trainings.frameworks.webdriver.listeners.BasicWebDriverListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;

import java.util.Map;

import static com.globant.automation.trainings.frameworks.webdriver.config.Framework.CONFIGURATION;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.remote.CapabilityType.PROXY;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Juan Krzemien
 */
public class WebDriverStrategyFactory {

    private static final Logger LOG = getLogger(WebDriverStrategyFactory.class);

    public WebDriver getDriverFor(Browser browser) {

        // Default is Firefox
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();

        switch (browser) {
            case CHROME:
                capabilities = DesiredCapabilities.chrome();
                break;
            case IE:
                capabilities = DesiredCapabilities.internetExplorer();
                break;
            case SAFARI:
                capabilities = DesiredCapabilities.safari();
                break;
            case ANDROID:
                capabilities = DesiredCapabilities.android();
                break;
            case IPHONE:
                capabilities = DesiredCapabilities.iphone();
                break;
            case IPAD:
                capabilities = DesiredCapabilities.ipad();
                break;
        }

        Map<String, Object> driverConfig = CONFIGURATION.Driver(browser).getCapabilities();

        capabilities.merge(new DesiredCapabilities(driverConfig));

        /**
         * Set proxy settings, if any
         */
        if (CONFIGURATION.Proxy().isEnabled()) {
            capabilities.setCapability(PROXY, CONFIGURATION.Proxy().getHost() + ":" + CONFIGURATION.Proxy().getPort());
        }

        return getDriverFor(capabilities);
    }

    public WebDriver getDriverFor(DesiredCapabilities capabilities) {
        /**
         * Create driver
         */
        WebDriver driver = new RemoteWebDriver(capabilities);

        driver.manage().timeouts().implicitlyWait(CONFIGURATION.WebDriver().getImplicitTimeOut(), SECONDS);
        driver.manage().timeouts().pageLoadTimeout(CONFIGURATION.WebDriver().getPageLoadTimeout(), SECONDS);
        driver.manage().timeouts().setScriptTimeout(CONFIGURATION.WebDriver().getScriptTimeout(), SECONDS);

        if (CONFIGURATION.WebDriver().isUseListener()) {
            /**
             * Wrap the driver as an event firing one, and add basic listener
             */
            final EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
            eventDriver.register(new BasicWebDriverListener());
            return eventDriver;
        }

        /**
         * Finally, return the driver instance
         */
        return driver;
    }

}
