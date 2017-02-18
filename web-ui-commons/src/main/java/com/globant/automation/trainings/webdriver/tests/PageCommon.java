package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.webdriver.webdriver.WebDriverDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfNestedElementLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

/**
 * Base class for modeling a Web Page using the Page Object pattern.
 * <p>
 * Does not include WEB related methods. Can be used for Appium native apps.
 *
 * @author Juan Krzemien
 */
public abstract class PageCommon extends WebDriverOperations {

    public PageCommon() {
        getLogger().info(format("Creating new [%s] Page Object instance...", toString()));
        initializePageObject(getDriver());
    }

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
        try {
            return waitFor(visibilityOf(element), 3, SECONDS, false).map(WebElement::isDisplayed).orElse(false);
        } catch (TimeoutException toe) {
            return false;
        }
    }

    /**
     * Retrieves a nested element from a root one (already detected)
     *
     * @param rootElement Root element
     * @param locator     Locator of sub element to find
     * @return Sub element detected or TimeOutException is thrown
     */
    protected WebElement getSubElementOf(WebElement rootElement, By locator) {
        Optional<WebElement> element = waitFor(presenceOfNestedElementLocatedBy(rootElement, locator));
        return element.orElse(null);
    }

}
