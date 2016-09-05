package com.globant.automation.trainings.frameworks.webdriver.webframework.tests.pageobject;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.util.List;

import static com.globant.automation.trainings.frameworks.webdriver.webframework.config.Framework.CONFIGURATION;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * This package local class contains most operation that WebDriver can perform
 * on a WebElement (click, select, type, etc) or on a Browser (switchTo, get URL/title, etc).
 *
 * @author Juan Krzemien
 */
class WebDriverOperations {

    protected static final Logger LOG = getLogger(PageObject.class);

    private final WebDriver driver;

    private final WebDriverWait wait;

    WebDriverOperations() {
        this.driver = Drivers.INSTANCE.get();

        wait = new WebDriverWait(driver, CONFIGURATION.WebDriver().getExplicitTimeOut());
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        wait.pollingEvery(CONFIGURATION.WebDriver().getPollingEveryMs(), MILLISECONDS);
    }

    protected WebDriver getDriver() {
        return driver;
    }

    /**
     * Invokes WebDriver's active waiting on an expected condition
     *
     * @param condition the {@link ExpectedCondition} to wait for. Choose from {@link org.openqa.selenium.support.ui.ExpectedConditions} or create your own.
     * @param <T>       Generic type on which to wait
     * @return Result from condition evaluation or a {@link TimeoutException} if operation times out.
     */
    public <T> T waitFor(ExpectedCondition<T> condition) {
        try {
            return wait.until(condition);
        } catch (TimeoutException toe) {
            String currentUrl = driver.getCurrentUrl();
            LOG.error(format("Error: %s\nCurrent URL: %s", toe.getMessage(), currentUrl), toe);
            throw toe;
        }
    }

    /**
     * Allows to switch WebDriver focus to a specific window or frame
     *
     * @return {@link org.openqa.selenium.WebDriver.TargetLocator} object from WebDriver instance
     */
    public WebDriver.TargetLocator switchTo() {
        return driver.switchTo();
    }

    /**
     * Closes current WebDriver window. <b>Does not shut down WebDriver server!</b>
     */
    public void closeWindow() {
        driver.close();
    }

    /**
     * Retrieves WebDriver's low level {@link org.openqa.selenium.interactions.Action} interactions API
     *
     * @return WebDriver's low level {@link org.openqa.selenium.interactions.Action} interactions API instance
     */
    protected Actions getActions() {
        return new Actions(driver);
    }

    /**
     * Types text or {@link Keys} on a {@link WebElement}.
     * <p>
     * Clears text field before typing.
     * <p>
     * Waits for element to be visible.
     *
     * @param element {@link WebElement} to type onto
     * @param text    Text or {@link Keys} to type
     */
    protected void type(WebElement element, CharSequence text) {
        waitFor(visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Selects a text/option, value or index from a {@link WebElement} (dropdown).
     * <p>
     * Waits for element to be visible.
     *
     * @param element {@link WebElement} to select from
     * @param option  Text/option/value/index to select
     */
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

    /**
     * Clicks on a {@link WebElement} using JavaScript (not regular WebDriver click).
     * <p>
     * Waits for element to be clickable.
     *
     * @param element The {@link WebElement} to click on
     */
    protected void jsClick(WebElement element) {
        waitFor(elementToBeClickable(element));
        getJS().executeScript("return arguments[0].click();", element);
    }

    /**
     * Clicks on a {@link WebElement}.
     * <p>
     * Waits for element to be clickable.
     *
     * @param element The {@link WebElement} to click on
     */
    protected void click(WebElement element) {
        waitFor(elementToBeClickable(element));
        element.click();
    }

    /**
     * Returns the value attribute of a {@link WebElement}.
     * <p>
     * Waits for element to be visible.
     *
     * @param element {@link WebElement} to retrieve value from
     * @return String containing the element's value
     */
    protected String getValue(WebElement element) {
        waitFor(visibilityOf(element));
        return element.getAttribute("value");
    }

    /**
     * Returns the text a {@link WebElement}.
     * <p>
     * Waits for element to be visible.
     *
     * @param element {@link WebElement} to retrieve text from
     * @return String containing the element's text
     */
    protected String getText(WebElement element) {
        waitFor(visibilityOf(element));
        return element.getText().trim();
    }

    /**
     * Retrieves selected text/option from a {@link WebElement} (dropdown).
     * <p>
     * Waits for element to be visible.
     *
     * @param element {@link WebElement} to retrieve value from
     * @return String containing the selected option/text
     */
    protected String getSelectedOption(WebElement element) {
        waitFor(visibilityOf(element));
        return new Select(element).getFirstSelectedOption().getText();
    }

    /**
     * Retrieves all selected texts/options from a {@link WebElement} (dropdown).
     * <p>
     * Waits for element to be visible.
     *
     * @param element {@link WebElement} to retrieve value from
     * @return List of strings containing the selected options/texts
     */
    protected List<String> getSelectedOptions(WebElement element) {
        waitFor(visibilityOf(element));
        return new Select(element).getAllSelectedOptions().stream().map(WebElement::getText).collect(toList());
    }

    /**
     * Retrieves WebDriver's Javascript executor instance
     *
     * @return WebDriver's Javascript executor instance
     */
    protected JavascriptExecutor getJS() {
        return (JavascriptExecutor) driver;
    }

    /**
     * Scrolls to the top of the web page
     */
    protected void scrollToTop() {
        getJS().executeScript("scrollTo(0,0);");
    }

    /**
     * Scrolls to a given {@link WebElement}.
     *
     * @param element {@link WebElement} to scroll to.
     */
    protected void scrollElementIntoView(WebElement element) {
        getJS().executeScript("return arguments[0].scrollIntoView(true);", element);
    }

}
