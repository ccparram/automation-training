package frameworks.web;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.listeners.BasicWebDriverListener;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

import static com.globant.automation.trainings.webdriver.config.Framework.CONFIGURATION;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.Platform.ANDROID;
import static org.openqa.selenium.remote.CapabilityType.PROXY;

/**
 * @author Juan Krzemien
 */
class WebDriverProvider implements Logging {

    WebDriver createDriverWith(Browser browser) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities(browser.getCapabilities());
        Optional<Map<String, Object>> driverCapabilities = ofNullable(CONFIGURATION.Driver(browser).getCapabilities());
        capabilities.merge(new DesiredCapabilities(driverCapabilities.orElse(emptyMap())));
        return createDriverWith(capabilities);
    }

    private WebDriver createDriverWith(DesiredCapabilities capabilities) throws MalformedURLException {
        setProxySettings(capabilities);
        getLogger().info("Creating RemoteWebDriver instance...");
        try {
            WebDriver driver;
            if (ANDROID.equals(capabilities.getPlatform())) {
                driver = new AndroidDriver<AndroidElement>(new URL(CONFIGURATION.WebDriver().getRemoteURL()), capabilities);
                getLogger().info("Setting up AndroidDriver timeout...");
                driver.manage().timeouts().implicitlyWait(CONFIGURATION.WebDriver().getImplicitTimeOut(), SECONDS);
            } else {
                driver = new RemoteWebDriver(new URL(CONFIGURATION.WebDriver().getRemoteURL()), capabilities);
                // TODO: APPIUM DOES NOT SUPPORT THIS!
                getLogger().info("Setting up WebDriver timeouts...");
                driver.manage().timeouts().implicitlyWait(CONFIGURATION.WebDriver().getImplicitTimeOut(), SECONDS);
                driver.manage().timeouts().pageLoadTimeout(CONFIGURATION.WebDriver().getPageLoadTimeout(), SECONDS);
                driver.manage().timeouts().setScriptTimeout(CONFIGURATION.WebDriver().getScriptTimeout(), SECONDS);
                driver.manage().window().maximize();
                driver = setListeners(driver);
            }
            return driver;
        } catch (Exception e) {
            getLogger().error(e.getLocalizedMessage(), e);
            throw e;
        }
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

}
