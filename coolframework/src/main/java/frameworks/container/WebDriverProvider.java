package frameworks.container;

import frameworks.listeners.BasicWebDriverListener;
import frameworks.logging.Logging;
import frameworks.web.Browser;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static frameworks.config.Framework.CONFIGURATION;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.remote.CapabilityType.PROXY;

/**
 * @author Juan Krzemien
 */
@Lazy
@Component
public class WebDriverProvider extends AbstractFactoryBean<WebDriver> implements Logging {

    private BrowserQueue browserQueue;

    @Autowired
    WebDriverProvider(BrowserQueue browserQueue) {
        this.browserQueue = browserQueue;
    }

    private WebDriver getDriverFor(Browser browser) throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities(browser.getCapabilities());

        Map<String, Object> driverConfig = CONFIGURATION.Driver(browser).getCapabilities();

        capabilities.merge(new DesiredCapabilities(driverConfig));

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

        return getDriverFor(capabilities);
    }

    private WebDriver getDriverFor(Capabilities capabilities) throws MalformedURLException {
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

    @Override
    public Class<?> getObjectType() {
        return WebDriver.class;
    }

    @Override
    protected WebDriver createInstance() throws Exception {
        return getDriverFor(browserQueue.get());
    }
}
