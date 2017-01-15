package com.globant.automation.trainings.webframework.listeners;

import com.globant.automation.trainings.webframework.logging.Logging;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.util.Arrays;

import static java.lang.String.format;

/**
 * Example of a basic WebDriver listener
 * <p>
 * Just logs WebDriver activity
 *
 * @author Juan Krzemien
 */
public class BasicWebDriverListener implements WebDriverEventListener, Logging {

    @Override
    public void afterClickOn(WebElement webElement, WebDriver driver) {
        getLogger().info("Clicked element " + webElement);
    }

    @Override
    public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
        getLogger().debug(format("Attempting to change value of [%s] to [%s]...", webElement, Arrays.toString(charSequences)));
    }

    @Override
    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
        getLogger().debug(format("Changed value of [%s] to [%s]...", webElement, Arrays.toString(charSequences)));
    }

    @Override
    public void afterFindBy(By locator, WebElement webElement, WebDriver driver) {
        getLogger().debug("Element identified with locator " + locator);
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        getLogger().info("Navigated back completed!");
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        getLogger().info("Navigated forward completed!");
    }

    @Override
    public void beforeNavigateRefresh(WebDriver driver) {
        getLogger().info("Refreshing page...");
    }

    @Override
    public void afterNavigateRefresh(WebDriver driver) {
        getLogger().info("Page refreshed");
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        getLogger().info("Navigated to " + url + " successfully!");
    }

    @Override
    public void afterScript(String script, WebDriver driver) {
        getLogger().debug("Javascript snippet executed: " + script);
    }

    @Override
    public void beforeClickOn(WebElement webElement, WebDriver driver) {
        getLogger().debug("Attempting to click " + webElement + "...");
    }

    @Override
    public void beforeFindBy(By locator, WebElement webElement, WebDriver driver) {
        getLogger().debug("Attempting to identify element with locator " + locator + "...");
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        getLogger().debug("Attempting to navigate back to previous page...");
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        getLogger().debug("Attempting to navigate forward to next page...");
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        getLogger().info("Navigating to URL: " + url);
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {
        getLogger().debug("Attempting to execute Javascript: " + script);
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        getLogger().debug("An exception occurred! Exception was: " + throwable.getMessage());
    }
}
