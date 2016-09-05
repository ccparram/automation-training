package com.globant.automation.trainings.frameworks.webdriver.webframework.listeners;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.slf4j.Logger;

import java.util.Arrays;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Example of a basic WebDriver listener
 * <p>
 * Just logs WebDriver activity
 *
 * @author Juan Krzemien
 */
public class BasicWebDriverListener implements WebDriverEventListener {

    private static final Logger LOG = getLogger(BasicWebDriverListener.class);

    @Override
    public void afterClickOn(WebElement webElement, WebDriver driver) {
        LOG.info("Clicked element " + webElement);
    }

    @Override
    public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
        LOG.debug(format("Attempting to change value of [%s] to [%s]...", webElement, Arrays.toString(charSequences)));
    }

    @Override
    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
        LOG.debug(format("Changed value of [%s] to [%s]...", webElement, Arrays.toString(charSequences)));
    }

    @Override
    public void afterFindBy(By locator, WebElement webElement, WebDriver driver) {
        LOG.debug("Element identified with locator " + locator);
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        LOG.info("Navigated back completed!");
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        LOG.info("Navigated forward completed!");
    }

    @Override
    public void beforeNavigateRefresh(WebDriver driver) {
        LOG.info("Refreshing page...");
    }

    @Override
    public void afterNavigateRefresh(WebDriver driver) {
        LOG.info("Page refreshed");
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        LOG.info("Navigated to " + url + " successfully!");
    }

    @Override
    public void afterScript(String script, WebDriver driver) {
        LOG.debug("Javascript snippet executed: " + script);
    }

    @Override
    public void beforeClickOn(WebElement webElement, WebDriver driver) {
        LOG.debug("Attempting to click " + webElement + "...");
    }

    @Override
    public void beforeFindBy(By locator, WebElement webElement, WebDriver driver) {
        LOG.debug("Attempting to identify element with locator " + locator + "...");
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        LOG.debug("Attempting to navigate back to previous page...");
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        LOG.debug("Attempting to navigate forward to next page...");
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        LOG.info("Navigating to URL: " + url);
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {
        LOG.debug("Attempting to execute Javascript: " + script);
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        LOG.debug("An exception occurred! Exception was: " + throwable.getMessage());
    }
}
