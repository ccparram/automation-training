package com.globant.automation.trainings.webdriver.listeners;

import com.globant.automation.trainings.logging.Logging;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

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
        getLogger().debug(String.format("Attempting to change value of [%s] to [%s]...", webElement, Arrays.toString(charSequences)));
    }

    @Override
    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
        getLogger().debug(String.format("Changed value of [%s] to [%s]...", webElement, Arrays.toString(charSequences)));
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
        if (driver instanceof TakesScreenshot) {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(scrFile, new File("screenShot" + LocalDateTime.now() + ".png"));
            } catch (IOException e) {
                getLogger().error(e.getLocalizedMessage(), e);
            }
        }
    }
}
