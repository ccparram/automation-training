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

import static com.globant.automation.trainings.config.CommonSettings.COMMON;
import static com.globant.automation.trainings.webdriver.browsers.Browser.*;
import static com.globant.automation.trainings.webdriver.config.UISettings.UI;
import static java.lang.Thread.currentThread;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.remote.CapabilityType.PROXY;

/**
 * Package local class factory in charge of instantiation of WebDriver objects
 *
 * @author Juan Krzemien
 */
class WebDriverFactory implements Logging {

    private static final Set<Browser> MOBILE_BROWSERS = Sets.immutableEnumSet(ANDROID, IPHONE, IPAD);

    WebDriverDecorator createFor(Browser browser) throws MalformedURLException {
        DesiredCapabilities capabilities = buildCapabilities(browser);
        WebDriver innerDriver;
        if (MOBILE_BROWSERS.contains(browser)) {
            innerDriver = new AppiumDriver<MobileElement>(new URL(UI.WebDriver().getRemoteURL()), capabilities);
        } else {
            innerDriver = new RemoteWebDriver(new URL(UI.WebDriver().getRemoteURL()), capabilities);
            innerDriver.manage().window().maximize();
        }
        WebDriverDecorator driver = new WebDriverDecorator(ThreadGuard.protect(innerDriver));
        setTimeOuts(browser, driver);
        return driver;
    }

    private DesiredCapabilities buildCapabilities(Browser browser) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        DesiredCapabilities fromConfigCapabilities = new DesiredCapabilities(UI.Driver(browser).getCapabilities());
        capabilities.merge(browser.getCapabilities());
        capabilities.merge(fromConfigCapabilities);
        if (browser == ANDROID) {
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
        driver.manage().timeouts().implicitlyWait(UI.WebDriver().getImplicitTimeOut(), SECONDS);
        if (!MOBILE_BROWSERS.contains(browser)) {
            driver.manage().timeouts().pageLoadTimeout(UI.WebDriver().getPageLoadTimeout(), SECONDS);
            driver.manage().timeouts().setScriptTimeout(UI.WebDriver().getScriptTimeout(), SECONDS);
        }
    }

    private DesiredCapabilities setProxySettings(DesiredCapabilities capabilities) {
        // Set proxy settings, if any
        if (COMMON.proxy().isEnabled()) {
            getLogger().info("Setting WebDriver proxy...");

            String proxyCfg = COMMON.proxy().getHost() + ":" + COMMON.proxy().getPort();

            capabilities.setCapability(PROXY, new Proxy()
                    .setHttpProxy(proxyCfg)
                    .setFtpProxy(proxyCfg)
                    .setSslProxy(proxyCfg)
                    .setNoProxy(COMMON.proxy().getNoProxyFor())
            );
        }
        return capabilities;
    }
}
