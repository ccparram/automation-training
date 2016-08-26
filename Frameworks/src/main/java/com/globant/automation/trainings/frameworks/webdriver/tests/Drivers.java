package com.globant.automation.trainings.frameworks.webdriver.tests;

import com.globant.automation.trainings.frameworks.webdriver.enums.Browser;
import com.globant.automation.trainings.frameworks.webdriver.listeners.BasicWebDriverListener;
import org.openqa.selenium.Proxy;
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
 * Global per thread web driver container
 *
 * @author Juan Krzemien
 */
enum Drivers {

    INSTANCE;

    private static final ThreadLocal<WebDriver> driverPerThread = new ThreadLocal<>();

    private final WebDriverStrategyFactory factory = new WebDriverStrategyFactory();

    public WebDriver create(Browser desiredBrowser) {
        WebDriver instance = get();
        if (instance == null) {
            instance = factory.getDriverFor(desiredBrowser);
            driverPerThread.set(instance);
        }
        return instance;
    }

    public WebDriver get() {
        return driverPerThread.get();
    }

    public void destroy() {
        WebDriver instance = driverPerThread.get();
        if (instance != null) {
            instance.quit();
        }
        driverPerThread.remove();
    }

    class WebDriverStrategyFactory {

        private final boolean debugMode = parseBoolean(getProperty("DEBUG_MODE", "false"));

        WebDriver getDriverFor(Browser browser) {

            DesiredCapabilities capabilities = new DesiredCapabilities(browser.getCapabilities());

            Map<String, Object> driverConfig = CONFIGURATION.Driver(browser).getCapabilities();

            // MARIONETTE: Promote capability to JVM property
            if (driverConfig.containsKey("firefox_binary")) {
                System.setProperty("webdriver.firefox.bin", driverConfig.get("firefox_binary").toString());
            }

            capabilities.merge(new DesiredCapabilities(driverConfig));

            // Set proxy settings, if any
            if (CONFIGURATION.Proxy().isEnabled()) {

                String proxyCfg = CONFIGURATION.Proxy().getHost() + ":" + CONFIGURATION.Proxy().getPort();

                Proxy proxy = new Proxy();
                proxy.setHttpProxy(proxyCfg)
                        .setFtpProxy(proxyCfg)
                        .setSslProxy(proxyCfg);

                capabilities.setCapability(PROXY, proxy);
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

}
