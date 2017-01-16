package frameworks.web;

import frameworks.listeners.BasicWebDriverListener;
import frameworks.logging.Logging;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static frameworks.config.Framework.CONFIGURATION;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.remote.CapabilityType.PROXY;

/**
 * @author Juan Krzemien
 */
@Lazy
@Component
public class WebDriverProvider implements Logging {

    private final BrowserQueue browserQueue;
    private ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public WebDriverProvider(BrowserQueue browserQueue) {
        this.browserQueue = browserQueue;
    }

    public WebDriver get() {

        WebDriver driver = driverThreadLocal.get();

        if (driver == null) {

            try {
                Browser browser = browserQueue.take();

                currentThread().setName(browser.name() + "-Thread");

                driver = createDriver(getDriverCapabilities(browser));

            } catch (InterruptedException | MalformedURLException e) {
                e.printStackTrace();
            }
            driverThreadLocal.set(driver);
        }

        return driver;
    }

    private DesiredCapabilities getDriverCapabilities(Browser browser) {
        DesiredCapabilities capabilities = new DesiredCapabilities(browser.getCapabilities());

        Map<String, Object> driverConfig = CONFIGURATION.Driver(browser).getCapabilities();

        capabilities.merge(new DesiredCapabilities(driverConfig));

        return capabilities;
    }

    private WebDriver createDriver(DesiredCapabilities capabilities) throws MalformedURLException {
        setProxySettings(capabilities);
        getLogger().info("Creating RemoteWebDriver instance...");
        WebDriver driver = new RemoteWebDriver(new URL(CONFIGURATION.WebDriver().getRemoteURL()), capabilities);
        getLogger().info("Setting up WebDriver timeouts...");
        driver.manage().timeouts().implicitlyWait(CONFIGURATION.WebDriver().getImplicitTimeOut(), SECONDS);
        driver.manage().timeouts().pageLoadTimeout(CONFIGURATION.WebDriver().getPageLoadTimeout(), SECONDS);
        driver.manage().timeouts().setScriptTimeout(CONFIGURATION.WebDriver().getScriptTimeout(), SECONDS);
        driver.manage().window().maximize();
        driver = setListeners(driver);
        return driver;
    }

    private void setProxySettings(DesiredCapabilities capabilities) {
        // Set proxy settings, if any
        if (CONFIGURATION.Proxy().isEnabled()) {
            getLogger().info("Setting WebDriver proxy...");

            String proxyCfg = CONFIGURATION.Proxy().getHost() + ":" + CONFIGURATION.Proxy().getPort();

            capabilities.setCapability(PROXY, new Proxy()
                    .setHttpProxy(proxyCfg)
                    .setFtpProxy(proxyCfg)
                    .setSslProxy(proxyCfg)
            );
        }
    }

    private WebDriver setListeners(WebDriver driver) {
        if (CONFIGURATION.WebDriver().isUseListener()) {
            getLogger().info("Setting up WebDriver listener...");
            // Wrap the driver as an event firing one, and add basic listener
            final EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
            eventDriver.register(new BasicWebDriverListener());
            driver = eventDriver;
        }
        return driver;
    }

    public void dispose() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
        }
        driverThreadLocal.set(null);
    }
}
