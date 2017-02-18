package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.webdriver.WebDriverDecorator;
import com.google.common.collect.Sets;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ThreadGuard;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Set;

import static com.globant.automation.trainings.webdriver.config.Framework.CONFIGURATION;
import static java.lang.Thread.currentThread;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.remote.CapabilityType.PROXY;

/**
 * @author Juan Krzemien
 */
class WebDriverFactory implements Logging {

    private static final Set<Browser> MOBILE_BROWSERS = Sets.immutableEnumSet(Browser.ANDROID, Browser.IPHONE, Browser.IPAD);

    WebDriverDecorator createFor(Browser browser) throws MalformedURLException {
        DesiredCapabilities capabilities = buildCapabilities(browser);
        WebDriver innerDriver;
        if (MOBILE_BROWSERS.contains(browser)) {
            innerDriver = new AppiumDriver<MobileElement>(new URL(CONFIGURATION.WebDriver().getRemoteURL()), capabilities);
        } else {
            innerDriver = new RemoteWebDriver(new URL(CONFIGURATION.WebDriver().getRemoteURL()), capabilities);
            innerDriver.manage().window().maximize();
        }
        WebDriverDecorator driver = new WebDriverDecorator(ThreadGuard.protect(innerDriver));
        setTimeOuts(browser, driver);
        return driver;
    }

    private DesiredCapabilities buildCapabilities(Browser browser) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        DesiredCapabilities fromConfigCapabilities = new DesiredCapabilities(CONFIGURATION.Driver(browser).getCapabilities());
        capabilities.merge(browser.getCapabilities());
        capabilities.merge(fromConfigCapabilities);
        if (browser == Browser.ANDROID) {
            ofNullable(capabilities.getCapability("app")).ifPresent(app -> {
                ofNullable(currentThread().getContextClassLoader().getResource((String) app)).ifPresent(appPath -> {
                    try {
                        capabilities.setCapability("app", Paths.get(appPath.toURI()).toFile().getAbsolutePath());
                    } catch (URISyntaxException e) {
                        getLogger().error("Could not determine app absolute path", e);
                    }
                });
            });
        }
        return setProxySettings(capabilities);
    }

    private void setTimeOuts(Browser browser, WebDriverDecorator driver) {
        driver.manage().timeouts().implicitlyWait(CONFIGURATION.WebDriver().getImplicitTimeOut(), SECONDS);
        if (!MOBILE_BROWSERS.contains(browser)) {
            driver.manage().timeouts().pageLoadTimeout(CONFIGURATION.WebDriver().getPageLoadTimeout(), SECONDS);
            driver.manage().timeouts().setScriptTimeout(CONFIGURATION.WebDriver().getScriptTimeout(), SECONDS);
        }
    }

    private DesiredCapabilities setProxySettings(DesiredCapabilities capabilities) {
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
        return capabilities;
    }
}
