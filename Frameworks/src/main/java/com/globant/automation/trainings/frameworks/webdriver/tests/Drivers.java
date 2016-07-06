package com.globant.automation.trainings.frameworks.webdriver.tests;

import com.globant.automation.trainings.frameworks.webdriver.enums.Browser;
import com.globant.automation.trainings.frameworks.webdriver.factory.WebDriverStrategyFactory;
import org.openqa.selenium.WebDriver;

/**
 * Sample global per thread web driver container
 *
 * @author Juan Krzemien
 */
enum Drivers {

    INSTANCES;

    private static ThreadLocal<WebDriver> driverPerThread = new ThreadLocal<>();
    private WebDriverStrategyFactory factory = new WebDriverStrategyFactory();

    public WebDriver create(Browser desiredBrowser) {
        WebDriver instance = driverPerThread.get();
        if (instance == null) {
            instance = factory.getDriverFor(desiredBrowser);
            driverPerThread.set(instance);
        }
        return instance;
    }

    public WebDriver get() {
        return driverPerThread.get();
    }

    public void destroy() {
        WebDriver instance = driverPerThread.get();
        if (instance != null) {
            instance.quit();
        }
        driverPerThread.remove();
    }

}
