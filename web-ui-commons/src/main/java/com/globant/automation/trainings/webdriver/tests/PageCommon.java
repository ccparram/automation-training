package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.webdriver.webdriver.WebDriverDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

/**
 * Base class for modeling a Web Page using the Page Object pattern.
 * <p>
 * Does not include WEB related methods. Can be used for Appium native apps.
 *
 * @author Juan Krzemien
 */
public abstract class PageCommon extends WebDriverOperations {

    protected PageCommon() {
        getLogger().info(format("Creating new [%s] Page Object instance...", toString()));
        initializePageObject(getDriver());
    }

    /**
     * Allows users to define how a Page Object should be initialized
     *
     * @param driver WebDriver instance to help with Page Object initialization
     */
    protected abstract void initializePageObject(WebDriverDecorator driver);

    /**
     * Pretty print POM name, when requested as a String
     *
     * @return the class name of the POM
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    /**
     * Waits for a little amount of time to confirm if an element is visible or no
     *
     * @param element Element to check for visibility
     * @return true if visible, false otherwise
     */
    protected boolean isVisible(WebElement element) {
        return waitFor(visibilityOf(element), 2, SECONDS, false)
                .map(WebElement::isDisplayed)
                .orElse(false);
    }

    /**
     * Waits for a little amount of time to confirm if an element is not visible
     *
     * @param element Element to check for visibility
     * @return true if visible, false otherwise
     */
    protected boolean isNotVisible(WebElement element) {
        return waitFor(invisibilityOf(element), 2, SECONDS, false).orElse(true);
    }

    /**
     * Waits for a little amount of time to confirm if elements are visible or not
     *
     * @param elements Elements to check for visibility
     * @return true if all elements visible, false otherwise
     */
    protected boolean areVisible(List<WebElement> elements) {
        return elements.stream().allMatch(this::isVisible);
    }

    /**
     * Retrieves a nested element using a locator from an already detected one (root element)
     *
     * @param rootElement Root element
     * @param locator     Locator of sub element to find
     * @return Sub element detected or TimeOutException is thrown
     */
    protected WebElement getSubElementOf(WebElement rootElement, By locator) {
        Optional<WebElement> element = waitFor(presenceOfNestedElementLocatedBy(rootElement, locator));
        return element.orElseThrow(() -> new NoSuchElementException(format("Could not find [%s] inside provided element structure", locator)));
    }

}
