package com.globant.automation.trainings.frameworks.webdriver.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;

import static com.globant.automation.trainings.frameworks.webdriver.config.Framework.CONFIGURATION;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Juan Krzemien on 8/24/2016.
 */
class WebDriverOperations {

    protected static final Logger LOG = getLogger(PageObject.class);

    protected final WebDriver driver;

    private final WebDriverWait wait;

    WebDriverOperations() {
        this.driver = Drivers.INSTANCE.get();

        wait = new WebDriverWait(driver, CONFIGURATION.WebDriver().getExplicitTimeOut());
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        wait.pollingEvery(CONFIGURATION.WebDriver().getPollingEveryMs(), MILLISECONDS);
    }

    public <T> T waitFor(ExpectedCondition<T> condition) {
        try {
            return wait.until(condition);
        } catch (TimeoutException toe) {
            String currentUrl = driver.getCurrentUrl();
            switchTo().defaultContent();
            String parentUrl = driver.getCurrentUrl();
            String msg1 = format("Error: %s\nCurrent URL: %s", toe.getMessage(), currentUrl);
            String msg2 = format("%s Parent URL: ", msg1, parentUrl);
            String msg = Objects.equals(parentUrl, currentUrl) ? msg1 : msg2;
            LOG.error(msg, toe);
            throw toe;
        }
    }

    protected WebDriver.TargetLocator switchTo() {
        return driver.switchTo();
    }

    protected void closeWindow() {
        driver.close();
    }

    protected Actions getActions() {
        return new Actions(driver);
    }

    protected void type(WebElement element, CharSequence text) {
        waitFor(visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    protected void select(WebElement element, String option) {
        waitFor(visibilityOf(element));
        Select select = new Select(element);
        try {
            select.selectByVisibleText(option);
        } catch (NoSuchElementException e) {
            try {
                select.selectByValue(option);
            } catch (NoSuchElementException ex) {
                select.selectByIndex(Integer.parseInt(option));
            }
        }
    }

    protected void jsClick(WebElement element) {
        waitFor(elementToBeClickable(element));
        getJS().executeScript("return arguments[0].click();", element);
    }

    protected void click(WebElement element) {
        waitFor(elementToBeClickable(element));
        element.click();
    }

    protected String getValue(WebElement element) {
        waitFor(visibilityOf(element));
        return element.getAttribute("value");
    }

    protected String getText(WebElement element) {
        waitFor(visibilityOf(element));
        return element.getText().trim();
    }

    protected String getSelectedOption(WebElement element) {
        waitFor(visibilityOf(element));
        return new Select(element).getFirstSelectedOption().getText();
    }

    protected List<String> getSelectedOptions(WebElement element) {
        waitFor(visibilityOf(element));
        return new Select(element).getAllSelectedOptions().stream().map(WebElement::getText).collect(toList());
    }

    protected JavascriptExecutor getJS() {
        return (JavascriptExecutor) driver;
    }

    protected void scrollToTop() {
        getJS().executeScript("scrollTo(0,0);");
    }

    protected void scrollElementIntoView(WebElement element) {
        getJS().executeScript("return arguments[0].scrollIntoView(true);", element);
    }

}
