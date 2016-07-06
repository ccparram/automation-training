package com.globant.automation.trainings.frameworks.webdriver.tests;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.globant.automation.trainings.frameworks.webdriver.config.Framework.CONFIGURATION;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.support.PageFactory.initElements;

/**
 * Sample base Page Object class
 *
 * @author Juan Krzemien
 */
public abstract class PageObject {

    protected final WebDriverWait wait;

    protected PageObject() {
        navigateToUrlIfDecorated();
        WebDriver driver = Drivers.INSTANCES.get();
        this.wait = new WebDriverWait(driver, CONFIGURATION.WebDriver().getImplicitTimeOut());
        wait.pollingEvery(500, MILLISECONDS)
                .withTimeout(CONFIGURATION.WebDriver().getImplicitTimeOut(), SECONDS)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class);
        initElements(driver, this);
    }

    private void navigateToUrlIfDecorated() {
        Url[] urls = getClass().getAnnotationsByType(Url.class);
        if (urls.length > 0) {
            Drivers.INSTANCES.get().get(urls[0].value());
        }
    }
}
