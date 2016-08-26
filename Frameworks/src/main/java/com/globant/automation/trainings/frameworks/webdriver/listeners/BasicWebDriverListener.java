package com.globant.automation.trainings.frameworks.webdriver.listeners;

import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

import static java.lang.Boolean.parseBoolean;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.getProperty;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.openqa.selenium.OutputType.FILE;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Example of a basic WebDriver listener
 * <p>
 * Just logs operations and takes screenshots upon exceptions
 *
 * @author Juan Krzemien
 */
public class BasicWebDriverListener implements WebDriverEventListener {

    private static final Logger LOG = getLogger(BasicWebDriverListener.class);
    private final boolean debugMode = parseBoolean(getProperty("DEBUG_MODE", "false"));

    @Override
    public void afterChangeValueOf(WebElement webElement, WebDriver driver) {
        if (debugMode) {
            LOG.info("Changed value for element " + webElement);
        }
    }

    @Override
    public void afterClickOn(WebElement webElement, WebDriver driver) {
        LOG.info("Clicked element " + webElement);
    }

    @Override
    public void afterFindBy(By locator, WebElement webElement, WebDriver driver) {
        if (debugMode) {
            LOG.info("Element identified with locator " + locator);
        }
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
        if (debugMode) {
            LOG.info("Javascript snippet executed: " + script);
        }
    }

    @Override
    public void beforeChangeValueOf(WebElement webElement, WebDriver driver) {
        if (debugMode) {
            LOG.info("Attempting to change value of " + webElement + "...");
        }
    }

    @Override
    public void beforeClickOn(WebElement webElement, WebDriver driver) {
        if (debugMode) {
            LOG.info("Attempting to click " + webElement + "...");
        }
    }

    @Override
    public void beforeFindBy(By locator, WebElement webElement, WebDriver driver) {
        if (debugMode) {
            LOG.info("Attempting to identify element with locator " + locator + "...");
        }
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        if (debugMode) {
            LOG.info("Attempting to navigate back to previous page...");
        }
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        if (debugMode) {
            LOG.info("Attempting to navigate forward to next page...");
        }
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        LOG.info("Navigating to URL: " + url);
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {
        if (debugMode) {
            LOG.info("Attempting to execute Javascript: " + script);
        }
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        if (driver instanceof TakesScreenshot) {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(FILE);
            try {
                copyFile(scrFile, new File("screenshot-" + currentTimeMillis() + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LOG.error("An exception occurred! Exception was: " + throwable.getMessage(), throwable);
    }
}
