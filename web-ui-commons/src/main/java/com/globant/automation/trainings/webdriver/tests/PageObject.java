package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.webdriver.annotations.DeletesCookies;
import com.globant.automation.trainings.webdriver.annotations.Url;
import com.globant.automation.trainings.webdriver.webdriver.WebDriverDecorator;
import com.globant.automation.trainings.webdriver.webdriver.WebElementDecorator;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.globant.automation.trainings.config.CommonSettings.COMMON;
import static com.globant.automation.trainings.logging.Reporter.REPORTER;
import static com.globant.automation.trainings.utils.Reflection.getFieldValuesAnnotatedWith;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

/**
 * Base class for modeling a Web Page using the Page Object pattern
 *
 * @author Juan Krzemien
 */
public class PageObject<T extends PageObject> extends PageCommon {

    public PageObject() {
        deleteCookiesIfDecorated();
        navigateToPartialUrlIfDecorated();
    }

    @Override
    protected void initializePageObject(WebDriverDecorator driver) {
        PageFactory.initElements(driver, this);
    }

    /**
     * Navigates to SUT_URL environmental variable (if present) or configured base URL
     *
     * @return same Page Object instance
     */
    public T open() {
        // Environment variable SUT_URL may override all...
        String url = getBaseUrlFromEnvironment().orElseGet(() -> COMMON.environment().getBaseUrl());
        goToUrl(url);
        return (T) this;
    }

    /**
     * If POM is annotated with @Url, navigates to that URL.
     */
    private void navigateToPartialUrlIfDecorated() {
        final Class<? extends PageObject> pomClass = getClass();
        ofNullable(pomClass.getAnnotation(Url.class)).ifPresent(url -> {
            String baseUrl = COMMON.environment().getBaseUrl();
            try {
                StringBuilder resource = new StringBuilder();
                if (url.value().startsWith("/")) {
                    resource.append(getBaseUrlFromEnvironment().orElse(baseUrl));
                }
                resource.append(url.value());
                URL builtUrl = new URL(resource.toString());
                if (!getCurrentUrl().startsWith("http")) {
                    getLogger().info(format("Page Object [%s] is marked with @Url, navigating to [%s]...", pomClass.getSimpleName(), builtUrl));
                    goToUrl(builtUrl.toString());
                }
            } catch (MalformedURLException e) {
                getLogger().error(format("Could not build a valid URL from %s + %s!", baseUrl, url.value()));
            }
        });
    }

    /**
     * Optionally retrieves SUT_URL environment variable value
     *
     * @return Optional with a possible String value representing the base URL
     */
    private Optional<String> getBaseUrlFromEnvironment() {
        Optional<String> sutUrl = ofNullable(System.getenv("SUT_URL"));
        sutUrl.ifPresent(url -> {
            REPORTER.warn("Environment variable SUT_URL present!: %s", toString());
            getLogger().info(format("Environment variable SUT_URL present!: %s", url));
        });
        return sutUrl;
    }

    /**
     * Navigates to a given URL as a string
     *
     * @param url
     */
    protected void goToUrl(String url) {
        REPORTER.info("Navigating to %s", url);
        getDriver().navigate().to(url);
    }

    /**
     * Deletes cookies in browser if DeletesCookies annotation is present in POM
     */
    private void deleteCookiesIfDecorated() {
        ofNullable(getClass().getAnnotation(DeletesCookies.class)).ifPresent(deletesCookies -> {
            REPORTER.info("Deleting cookies at %s page", toString());
            getLogger().info(format("Page Object [%s] is marked with @DeletesCookies, deleting...", toString()));
            getDriver().manage().deleteAllCookies();
        });
    }

    /**
     * Allows to switch getWebDriver focus to a specific window or frame
     * <p>
     * This method was kept public since there is a chance of using this framework with
     * Gherkin like test runners which *may* need access to Switch context feature from outside POMs.
     *
     * @return {@link WebDriver.TargetLocator} object from getWebDriver instance
     */
    protected WebDriver.TargetLocator switchTo() {
        return getDriver().switchTo();
    }

    /**
     * Closes current getWebDriver window. <b>Does not shut down getWebDriver server!</b>
     */
    public void closeWindow() {
        REPORTER.info("Closing browser window (not quitting driver)");
        getDriver().close();
    }

    /**
     * Refreshes browser window
     */
    public void refresh() {
        REPORTER.info("Refreshing %s page", toString());
        getDriver().navigate().refresh();
        switchTo().defaultContent();
    }

    /**
     * Retrieves getWebDriver's Javascript executor instance
     *
     * @return getWebDriver's Javascript executor instance
     */
    protected JavascriptExecutor getJS() {
        return getDriver();
    }

    /**
     * Resizes the browser window to a desired dimension
     *
     * @param dimension Dimension to resize to
     */
    public void setWindowSizeTo(Dimension dimension) {
        REPORTER.info("Resizing window to %s", dimension);
        getDriver().manage().window().setSize(dimension);
    }

    /**
     * Clicks on a {@link WebElement} using JavaScript (not regular getWebDriver click).
     * <p>
     * Waits for element to be clickable.
     *
     * @param element The {@link WebElement} to click on
     */
    protected void jsClick(WebElement element) {
        waitFor(elementToBeClickable(element));
        getJS().executeScript("return arguments[0].click();",
                element instanceof WebElementDecorator
                        ?
                        ((WebElementDecorator) element).getWrappedElement()
                        :
                        element);
    }

    /**
     * Retrieves window title
     *
     * @return Browser's window title
     */
    public String getPageTitle() {
        return getDriver().getTitle();
    }

    /**
     * Switches WebDriver focus to a window other than current one.
     * Expects to find 2 windows (current and switchable new one).
     * Throws a TimeOutException if there are no 2 windows before timeout.
     */
    protected void switchWindows() {
        REPORTER.info("Switching to new window from %s page", toString());
        waitFor(numberOfWindowsToBe(2));
        String currentWindow = getDriver().getWindowHandle();
        Set<String> openWindows = new LinkedHashSet<>(getDriver().getWindowHandles());
        for (String openWindow : openWindows) {
            if (!openWindow.equals(currentWindow)) {
                switchTo().window(openWindow);
                break;
            }
        }
    }

    /**
     * Retrieves browser current URL
     *
     * @return The current URL
     */
    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    /**
     * Collects all fields in POM's hierarchy annotated with FindBy
     *
     * @return
     */
    protected List<?> getOwnWebElements() {
        return getFieldValuesAnnotatedWith(this, FindBy.class);
    }

    /**
     * Scrolls to the top of the web page
     */
    public void scrollToTop() {
        REPORTER.info("Scrolling to top of %s page", toString());
        getJS().executeScript("scrollTo(0,0);");
    }

    /**
     * Scrolls to the bottom of the web page
     */
    public void scrollToBottom() {
        REPORTER.info("Scrolling to bottom of %s page", toString());
        getJS().executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Scrolls to a given {@link WebElement}.
     *
     * @param element {@link WebElement} to scroll to.
     */
    protected void scrollElementIntoView(WebElement element) {
        REPORTER.info("Scrolling until element %s comes into view in %s page", element.toString(), toString());
        getJS().executeScript("return arguments[0].scrollIntoView(true);", element);
    }

}
