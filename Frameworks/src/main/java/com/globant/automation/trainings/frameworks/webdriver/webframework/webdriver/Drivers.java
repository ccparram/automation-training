package com.globant.automation.trainings.frameworks.webdriver.webframework.webdriver;

import com.globant.automation.trainings.frameworks.webdriver.webframework.events.messages.interfaces.IPageObjectCreatedEvent;
import com.globant.automation.trainings.frameworks.webdriver.webframework.events.messages.interfaces.ITestFinishedEvent;
import com.globant.automation.trainings.frameworks.webdriver.webframework.events.messages.interfaces.ITestStartedEvent;
import com.globant.automation.trainings.frameworks.webdriver.webframework.listeners.BasicWebDriverListener;
import com.globant.automation.trainings.frameworks.webdriver.webframework.logging.Logging;
import com.globant.automation.trainings.frameworks.webdriver.webframework.pageobject.PageObject;
import net.engio.mbassy.listener.Handler;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.Map;

import static com.globant.automation.trainings.frameworks.webdriver.webframework.config.Framework.CONFIGURATION;
import static com.globant.automation.trainings.frameworks.webdriver.webframework.events.EventBus.FRAMEWORK;
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

    private static final ThreadLocal<WebDriver> DriverPerThread = new ThreadLocal<>();
    private final WebDriverStrategyFactory factory = new WebDriverStrategyFactory();

    Drivers() {
        getLogger().info("Initializing WebDriver Thread Manager...");
        FRAMEWORK.suscribe(this);
    }

    @Handler
    private WebDriver createBrowserEventHandler(ITestStartedEvent event) {
        getLogger().info(format("Creating a [%s] browser instance...", event.getBrowser().name()));
        return create(event.getBrowser());
    }

    public WebDriver create(Browser desiredBrowser) {
        WebDriver instance = get();
        if (instance == null) {
            instance = factory.getDriverFor(desiredBrowser);
            DriverPerThread.set(instance);
        }
        return instance;
    }

    @Handler
    private void injectDriverInPageObject(IPageObjectCreatedEvent event) {
        PageObject po = event.getPageObject();
        getLogger().info(format("Injecting WebDriver instance into Page Object [%s]...", po.getClass().getSimpleName()));
        WebDriver driver = get();
        po.setDriver(driver);
    }

    public WebDriver get() {
        return DriverPerThread.get();
    }

    @Handler
    private void destroyBrowserEventHandler(ITestFinishedEvent event) {
        getLogger().info(format("Destroying [%s] browser instance...", event.getBrowser().name()));
        destroy();
    }

    public void destroy() {
        WebDriver instance = DriverPerThread.get();
        if (instance != null) {
            instance.quit();
        }
        DriverPerThread.remove();
    }

    static class WebDriverStrategyFactory implements Logging {

        private WebDriver getDriverFor(Browser browser) {

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

        private WebDriver getDriverFor(DesiredCapabilities capabilities) {
            getLogger().info("Creating RemoteWebDriver instance...");
            WebDriver driver = new RemoteWebDriver(CONFIGURATION.WebDriver().getRemoteURL(), capabilities);

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
