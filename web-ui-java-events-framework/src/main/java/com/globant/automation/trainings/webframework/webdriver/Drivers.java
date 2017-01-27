package com.globant.automation.trainings.webframework.webdriver;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.listeners.BasicWebDriverListener;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static com.globant.automation.trainings.webdriver.config.Framework.CONFIGURATION;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.remote.CapabilityType.PROXY;

/**
 * Package local (per thread) web driver instance initializer and container
 *
 * @author Juan Krzemien
 */
enum Drivers implements Logging {

    INSTANCE;

    private static final ThreadLocal<WebDriver> DRIVER_PER_THREAD = new ThreadLocal<>();
    private static final WebDriverStrategyFactory FACTORY = new WebDriverStrategyFactory();

    Drivers() {
        getLogger().info("Initializing WebDriver Thread Manager...");
        DriverEventsHandler.INSTANCE.init();
    }

    public WebDriver create(Browser browser) throws MalformedURLException {
        WebDriver instance = get();
        if (instance == null) {
            getLogger().info(format("Creating RemoteWebDriver for %s instance...", browser));
            instance = FACTORY.getDriverFor(browser);
            DRIVER_PER_THREAD.set(instance);
        }
        return instance;
    }

    public WebDriver get() {
        return DRIVER_PER_THREAD.get();
    }

    public void destroy() {
        WebDriver instance = DRIVER_PER_THREAD.get();
        if (instance != null) {
            instance.quit();
        }
        DRIVER_PER_THREAD.remove();
    }

    static class WebDriverStrategyFactory implements Logging {

        private WebDriver getDriverFor(Browser browser) throws MalformedURLException {
            DesiredCapabilities capabilities = defineCapabilities(browser);
            setProxy(capabilities);
            return getDriverFor(capabilities);
        }

        private DesiredCapabilities defineCapabilities(Browser browser) {
            DesiredCapabilities capabilities = new DesiredCapabilities(browser.getCapabilities());
            Map<String, Object> driverConfig = CONFIGURATION.Driver(browser).getCapabilities();
            capabilities.merge(new DesiredCapabilities(driverConfig));
            return capabilities;
        }

        private void setProxy(DesiredCapabilities capabilities) {
            // Set proxy settings, if any
            if (CONFIGURATION.Proxy().isEnabled()) {
                getLogger().info("Setting WebDriver proxy...");

                String proxyCfg = CONFIGURATION.Proxy().getHost() + ":" + CONFIGURATION.Proxy().getPort();

                Proxy proxy = new Proxy();
                proxy.setHttpProxy(proxyCfg)
                        .setFtpProxy(proxyCfg)
                        .setSslProxy(proxyCfg);

                capabilities.setCapability(PROXY, proxy);
            }
        }

        private WebDriver getDriverFor(DesiredCapabilities capabilities) throws MalformedURLException {
            WebDriver driver = new RemoteWebDriver(new URL(CONFIGURATION.WebDriver().getRemoteURL()), capabilities);
            getLogger().info("Setting up WebDriver timeouts...");
            driver.manage().timeouts().implicitlyWait(CONFIGURATION.WebDriver().getImplicitTimeOut(), SECONDS);
            driver.manage().timeouts().pageLoadTimeout(CONFIGURATION.WebDriver().getPageLoadTimeout(), SECONDS);
            driver.manage().timeouts().setScriptTimeout(CONFIGURATION.WebDriver().getScriptTimeout(), SECONDS);
            driver.manage().window().maximize();
            return setListener(driver);
        }

        private WebDriver setListener(WebDriver driver) {
            if (CONFIGURATION.WebDriver().isUseListener()) {
                getLogger().info("Setting up WebDriver listener...");
                // Wrap the driver as an event firing one, and add basic listener
                final EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
                eventDriver.register(new BasicWebDriverListener());
                driver = eventDriver;
            }
            return driver;
        }
    }
}
