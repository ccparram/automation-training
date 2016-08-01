package com.globant.automation.trainings.frameworks.webdriver.factories;

import com.globant.automation.trainings.frameworks.webdriver.enums.Browser;
import org.openqa.selenium.WebDriver;

/**
 * Sample global per thread web driver container
 *
 * @author Juan Krzemien
 */
public enum Drivers {

    INSTANCES;

    private static ThreadLocal<WebDriver> driverPerThread = new ThreadLocal<>();
    private static ThreadLocal<Browser> browserPerThread = new ThreadLocal<>();

    private final WebDriverStrategyFactory factory = new WebDriverStrategyFactory();

    public WebDriver create(Browser desiredBrowser) {
        WebDriver instance = driverPerThread.get();
        if (instance == null) {
            instance = factory.getDriverFor(desiredBrowser);
            driverPerThread.set(instance);
            browserPerThread.set(desiredBrowser);
        }
        return instance;
    }

    public WebDriver get() {
        return driverPerThread.get();
    }

    public Browser getBrowserType() {
        return browserPerThread.get();
    }

    public void destroy() {
        WebDriver instance = driverPerThread.get();
        if (instance != null) {
            instance.quit();
        }
        driverPerThread.remove();
        browserPerThread.remove();
    }

}
