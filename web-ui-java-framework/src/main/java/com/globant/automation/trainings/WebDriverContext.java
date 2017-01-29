package com.globant.automation.trainings;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import org.openqa.selenium.WebDriver;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

/**
 * Storage for *all* WebDriver instances for every thread.
 *
 * @author Juan Krzemien
 */
enum WebDriverContext implements Logging {

    WEB_DRIVER_CONTEXT;

    private static final AtomicInteger DRIVER_INSTANCES = new AtomicInteger();
    private static final ThreadLocal<BrowserDriverPair> DRIVERS_PER_THREAD = new ThreadLocal<>();

    public void set(BrowserDriverPair browserDriverPair) {
        getLogger().info(format("Active WebDriver instances: %d", DRIVER_INSTANCES.incrementAndGet()));
        DRIVERS_PER_THREAD.set(browserDriverPair);
    }

    public BrowserDriverPair get() {
        return DRIVERS_PER_THREAD.get();
    }

    public void remove() {
        Optional.ofNullable(get()).ifPresent(pair -> {
            getLogger().info(format("Active WebDriver instances: %d", DRIVER_INSTANCES.decrementAndGet()));
            pair.getDriver().quit();
        });
        DRIVERS_PER_THREAD.remove();
    }

    public static class BrowserDriverPair {
        private final Browser browser;
        private final WebDriver driver;

        BrowserDriverPair(Browser browser, WebDriver driver) {
            this.browser = browser;
            this.driver = driver;
        }

        public Browser getBrowser() {
            return browser;
        }

        public WebDriver getDriver() {
            return driver;
        }
    }

}
