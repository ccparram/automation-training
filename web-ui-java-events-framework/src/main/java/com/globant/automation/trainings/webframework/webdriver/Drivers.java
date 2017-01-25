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
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.remote.CapabilityType.PROXY;

/**
 * Package local (per thread) web driver instance initializer and container
 *
 * @author Juan Krzemien
 */
enum Drivers implements Logging {

    INSTANCE;

    private static final ThreadLocal<WebDriver> DriverPerThread = new ThreadLocal<>();
    private static final WebDriverStrategyFactory factory = new WebDriverStrategyFactory();

    Drivers() {
        getLogger().info("Initializing WebDriver Thread Manager...");
//        FRAMEWORK.suscribe(this);
        DriverEventsHandler.INSTANCE.init();
    }

    public WebDriver create(Browser desiredBrowser) throws MalformedURLException {
        WebDriver instance = get();
        if (instance == null) {
            instance = factory.getDriverFor(desiredBrowser);
            DriverPerThread.set(instance);
        }
        return instance;
    }

    public WebDriver get() {
        return DriverPerThread.get();
    }

    public void destroy() {
        WebDriver instance = DriverPerThread.get();
        if (instance != null) {
            instance.quit();
        }
        DriverPerThread.remove();
    }

    static class WebDriverStrategyFactory implements Logging {

        private WebDriver getDriverFor(Browser browser) throws MalformedURLException {

            DesiredCapabilities capabilities = new DesiredCapabilities(browser.getCapabilities());

            Map<String, Object> driverConfig = CONFIGURATION.Driver(browser).getCapabilities();

            capabilities.merge(new DesiredCapabilities(driverConfig));

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

            return getDriverFor(capabilities);
        }

        private WebDriver getDriverFor(DesiredCapabilities capabilities) throws MalformedURLException {
            getLogger().info("Creating RemoteWebDriver instance...");
            WebDriver driver = new RemoteWebDriver(new URL(CONFIGURATION.WebDriver().getRemoteURL()), capabilities);

            getLogger().info("Setting up WebDriver timeouts...");
            driver.manage().timeouts().implicitlyWait(CONFIGURATION.WebDriver().getImplicitTimeOut(), SECONDS);
            driver.manage().timeouts().pageLoadTimeout(CONFIGURATION.WebDriver().getPageLoadTimeout(), SECONDS);
            driver.manage().timeouts().setScriptTimeout(CONFIGURATION.WebDriver().getScriptTimeout(), SECONDS);

            driver.manage().window().maximize();

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
