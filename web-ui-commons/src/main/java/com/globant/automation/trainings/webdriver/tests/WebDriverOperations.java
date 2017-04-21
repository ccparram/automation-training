package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.runner.TestContext;
import com.globant.automation.trainings.webdriver.webdriver.WebDriverDecorator;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.globant.automation.trainings.webdriver.config.UISettings.UI;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

/**
 * This package local class contains most operation that WebDriver can perform
 * on a WebElement (click, select, type, etc). Explicit wait usage enforced.
 *
 * @author Juan Krzemien
 */
abstract class WebDriverOperations implements Logging {

    protected WebDriverDecorator getDriver() {
        return ((UIContext) TestContext.get()).getDriver();
    }

    /**
     * Invokes getWebDriver's active waiting on an expected condition
     * <p>
     * This method was kept public since there is a chance of using this framework with
     * Gherkin like test runners which *may* need access to wait feature from outside POMs.
     *
     * @param condition the {@link ExpectedCondition} to wait for. Choose from {@link org.openqa.selenium.support.ui.ExpectedConditions} or create your own.
     * @param <K>       Generic type on which to wait
     * @return Result from condition evaluation or a {@link TimeoutException} if operation times out.
     */
    protected <K> Optional<K> waitFor(ExpectedCondition<K> condition) {
        return waitFor(condition, UI.WebDriver().getExplicitTimeOut(), SECONDS, true);
    }

    protected <K> Optional<K> waitFor(ExpectedCondition<K> condition, int time, TimeUnit unit, boolean shouldFail) {
        WebDriverDecorator driver = getDriver();
        try {
            return Optional.of(
                    new WebDriverWait(driver.getWrappedDriver(), time)
                            .ignoring(NoSuchElementException.class)
                            .ignoring(StaleElementReferenceException.class)
                            .pollingEvery(UI.WebDriver().getPollingEveryMs(), MILLISECONDS)
                            .withTimeout(time, unit)
                            .until(condition)
            );
        } catch (TimeoutException toe) {
            if (shouldFail) {
                throw toe;
            }
            return Optional.empty();
        }
    }

    /**
     * Retrieves getWebDriver's low level {@link org.openqa.selenium.interactions.Action} interactions API
     *
     * @return getWebDriver's low level {@link org.openqa.selenium.interactions.Action} interactions API instance
     */
    protected Actions getActions() {
        return new Actions(getDriver());
    }

    /**
     * Types text or {@link Keys} on a {@link WebElement}.
     * <p>
     * Waits for element to be visible.
     *
     * @param element    {@link WebElement} to type onto
     * @param text       Text or {@link Keys} to type
     * @param cleanFirst Flag denoting if field should be cleared out before typing into it
     */
    protected void type(WebElement element, CharSequence text, boolean cleanFirst) {
        waitFor(visibilityOf(element)).orElseThrow(() -> new IllegalArgumentException("Element is not there!"));
        if (cleanFirst) {
            element.clear();
        }
        element.sendKeys(text);
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
        type(element, text, true);
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
        select(new Select(element), option);
    }

    /**
     * Selects an index from a {@link WebElement} (dropdown).
     * <p>
     * Waits for element to be visible.
     *
     * @param element {@link WebElement} to select from
     * @param option  Text/option/value/index to select
     */
    protected void select(WebElement element, int option) {
        waitFor(visibilityOf(element));
        select(new Select(element), option);
    }

    /**
     * Selects a text/option, value or index from a {@link WebElement} (dropdown).
     * <p>
     * Waits for element to be visible.
     *
     * @param select {@link Select} to select from
     * @param option Text/option/value/index to select
     */
    protected void select(Select select, String option) {
        try {
            select.selectByVisibleText(option);
        } catch (NoSuchElementException e) {
            getLogger().debug(e.getLocalizedMessage(), e);
            select.selectByValue(option);
        }
    }

    /**
     * Selects an index from a {@link WebElement} (dropdown).
     * <p>
     * Waits for element to be visible.
     *
     * @param select {@link Select} to select from
     * @param option Text/option/value/index to select
     */
    protected void select(Select select, int option) {
        select.selectByIndex(option);
    }

    /**
     * Checks a {@link WebElement} representing a checkbox.
     * <p>
     * Waits for element to be visible.
     *
     * @param element {@link WebElement} to check
     */
    protected void check(WebElement element) {
        waitFor(elementToBeClickable(element));
        if (element.getAttribute("checked") == null) {
            click(element);
        }
    }

    /**
     * Unchecks a {@link WebElement} representing a checkbox.
     * <p>
     * Waits for element to be visible.
     *
     * @param element {@link WebElement} to un-check
     */
    protected void uncheck(WebElement element) {
        waitFor(elementToBeClickable(element));
        if (element.getAttribute("checked") != null) {
            return;
        }
        click(element);
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
        String text = waitFor(visibilityOf(element)).map(WebElement::getText).orElse("");
        return text.isEmpty() ? element.getAttribute("value") : text;
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

}
