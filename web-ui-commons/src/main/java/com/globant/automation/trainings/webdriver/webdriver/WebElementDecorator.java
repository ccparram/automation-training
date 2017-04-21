package com.globant.automation.trainings.webdriver.webdriver;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.*;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.globant.automation.trainings.logging.Reporter.REPORTER;
import static com.globant.automation.trainings.webdriver.config.UISettings.UI;
import static java.lang.String.format;
import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

/**
 * Decorator for WebElement instances.
 * <p>
 * Allows for future interception of all WebElement related methods, if needed.
 * Also, allows for registering listeners to WebElement instances.
 *
 * @author Juan Krzemien
 */
public class WebElementDecorator implements WebElement, FindsByLinkText, FindsById, FindsByName,
        FindsByTagName, FindsByClassName, FindsByCssSelector,
        FindsByXPath, WrapsDriver, Locatable,
        TakesScreenshot, WrapsElement {

    private final WebDriver driver;
    private final Set<WebDriverEventListener> listeners = new HashSet<>();
    private final WebElement delegate;
    private final By locator;

    WebElementDecorator(WebElement element, WebDriver driver, By locator) {
        this.delegate = (WebElement) newProxyInstance(getClass().getClassLoader(), new Class[]{
                        WebElement.class, FindsByLinkText.class, FindsById.class, FindsByName.class,
                        FindsByTagName.class, FindsByClassName.class, FindsByCssSelector.class,
                        FindsByXPath.class, WrapsDriver.class, Locatable.class,
                        TakesScreenshot.class},
                new RefreshOnStaleInvocationHandler(element, locator));
        this.driver = driver;
        this.locator = locator;
    }

    public void registerListener(WebDriverEventListener listener) {
        listeners.add(listener);
    }

    private void propagateException(Exception e) {
        listeners.forEach(l -> l.onException(e, driver));
    }

    @Override
    public void click() {
        listeners.forEach(l -> l.beforeClickOn(delegate, driver));
        try {
            delegate.click();
            listeners.forEach(l -> l.afterClickOn(delegate, driver));
        } catch (Exception e) {
            propagateException(e);
            throw e;
        }
    }

    @Override
    public void submit() {
        delegate.submit();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        listeners.forEach(l -> l.beforeChangeValueOf(delegate, driver, charSequences));
        try {
            delegate.sendKeys(charSequences);
            listeners.forEach(l -> l.afterChangeValueOf(delegate, driver, charSequences));
        } catch (Exception e) {
            propagateException(e);
            throw e;
        }
    }

    @Override
    public void clear() {
        listeners.forEach(l -> l.beforeChangeValueOf(delegate, driver, new CharSequence[0]));
        try {
            delegate.clear();
            listeners.forEach(l -> l.afterChangeValueOf(delegate, driver, new CharSequence[0]));
        } catch (Exception e) {
            propagateException(e);
            throw e;
        }
    }

    @Override
    public String getTagName() {
        return delegate.getTagName();
    }

    @Override
    public String getAttribute(String attribute) {
        return delegate.getAttribute(attribute);
    }

    @Override
    public boolean isSelected() {
        return delegate.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return delegate.isEnabled();
    }

    @Override
    public String getText() {
        return delegate.getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        listeners.forEach(l -> l.beforeFindBy(by, delegate, driver));
        try {
            List<WebElement> result = delegate.findElements(by);
            listeners.forEach(l -> l.afterFindBy(by, delegate, driver));
            return result.stream().map(element -> new WebElementDecorator(element, driver, by)).collect(toList());
        } catch (Exception e) {
            propagateException(e);
            throw e;
        }
    }

    @Override
    public WebElement findElement(By by) {
        listeners.forEach(l -> l.beforeFindBy(by, delegate, driver));
        try {
            WebElement result = delegate.findElement(by);
            listeners.forEach(l -> l.afterFindBy(by, delegate, driver));
            return new WebElementDecorator(result, driver, by);
        } catch (Exception e) {
            propagateException(e);
            throw e;
        }
    }

    @Override
    public boolean isDisplayed() {
        return delegate.isDisplayed();
    }

    @Override
    public Point getLocation() {
        return delegate.getLocation();
    }

    @Override
    public Dimension getSize() {
        return delegate.getSize();
    }

    @Override
    public Rectangle getRect() {
        return delegate.getRect();
    }

    @Override
    public String getCssValue(String s) {
        return delegate.getCssValue(s);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return delegate.getScreenshotAs(outputType);
    }

    @Override
    public WebElement getWrappedElement() {
        RefreshOnStaleInvocationHandler ih = (RefreshOnStaleInvocationHandler) Proxy.getInvocationHandler(delegate);
        return ih.getWrappedElement();
    }

    @Override
    public String toString() {
        return format("%s wrapping a %s", getClass().getSimpleName(), locator.toString());
    }

    @Override
    public Coordinates getCoordinates() {
        return ((Locatable) delegate).getCoordinates();
    }

    @Override
    public WebElement findElementByClassName(String s) {
        return ((FindsByClassName) delegate).findElementByClassName(s);
    }

    @Override
    public List<WebElement> findElementsByClassName(String s) {
        return ((FindsByClassName) delegate).findElementsByClassName(s);
    }

    @Override
    public WebElement findElementByCssSelector(String s) {
        return ((FindsByCssSelector) delegate).findElementByCssSelector(s);
    }

    @Override
    public List<WebElement> findElementsByCssSelector(String s) {
        return ((FindsByCssSelector) delegate).findElementsByCssSelector(s);
    }

    @Override
    public WebElement findElementById(String s) {
        return ((FindsById) delegate).findElementById(s);
    }

    @Override
    public List<WebElement> findElementsById(String s) {
        return ((FindsById) delegate).findElementsById(s);
    }

    @Override
    public WebElement findElementByLinkText(String s) {
        return ((FindsByLinkText) delegate).findElementByLinkText(s);
    }

    @Override
    public List<WebElement> findElementsByLinkText(String s) {
        return ((FindsByLinkText) delegate).findElementsByLinkText(s);
    }

    @Override
    public WebElement findElementByPartialLinkText(String s) {
        return ((FindsByLinkText) delegate).findElementByPartialLinkText(s);
    }

    @Override
    public List<WebElement> findElementsByPartialLinkText(String s) {
        return ((FindsByLinkText) delegate).findElementsByPartialLinkText(s);
    }

    @Override
    public WebElement findElementByName(String s) {
        return ((FindsByName) delegate).findElementByName(s);
    }

    @Override
    public List<WebElement> findElementsByName(String s) {
        return ((FindsByName) delegate).findElementsByName(s);
    }

    @Override
    public WebElement findElementByTagName(String s) {
        return ((FindsByTagName) delegate).findElementByTagName(s);
    }

    @Override
    public List<WebElement> findElementsByTagName(String s) {
        return ((FindsByTagName) delegate).findElementsByTagName(s);
    }

    @Override
    public WebElement findElementByXPath(String s) {
        return ((FindsByXPath) delegate).findElementByXPath(s);
    }

    @Override
    public List<WebElement> findElementsByXPath(String s) {
        return ((FindsByXPath) delegate).findElementsByXPath(s);
    }

    @Override
    public WebDriver getWrappedDriver() {
        return driver;
    }

    private class RefreshOnStaleInvocationHandler implements InvocationHandler {

        private final By locator;
        private WebElement e;

        RefreshOnStaleInvocationHandler(WebElement element, By locator) {
            this.e = element;
            this.locator = locator;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                return method.invoke(e, args);
            } catch (InvocationTargetException ex) {
                Throwable innerEx = ex.getTargetException();
                if (innerEx != null) {
                    if (innerEx instanceof StaleElementReferenceException) {
                        REPORTER.debug("Locator [%s] caused a StaleElementReferenceException, attempting to refresh element...", locator);
                        e = new WebDriverWait(driver, UI.WebDriver().getExplicitTimeOut())
                                .pollingEvery(UI.WebDriver().getPollingEveryMs(), MILLISECONDS)
                                .withMessage(format("Timed out while refreshing locator %s while handling a StaleElementReferenceException!", locator))
                                .until(presenceOfElementLocated(locator));
                        REPORTER.debug("Element refreshed from locator [%s]. Re-attempting last action (%s)...", locator, method.getName());
                        return method.invoke(e, args);
                    } else if (innerEx instanceof NoSuchElementException) {
                        throw innerEx;
                    } else {
                        REPORTER.error(innerEx.getLocalizedMessage());
                        throw innerEx;
                    }
                }
                throw ex;
            }
        }

        WebElement getWrappedElement() {
            return e;
        }
    }
}
