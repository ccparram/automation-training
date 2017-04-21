package com.globant.automation.trainings.webdriver.webdriver;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Decorator for WebDriver instances.
 * <p>
 * Allows for future interception of all WebDriver related methods, if needed.
 * Also, allows for registering listeners to WebDriver instances.
 *
 * @author Juan Krzemien
 */
public class WebDriverDecorator implements WebDriver, JavascriptExecutor, TakesScreenshot, WrapsDriver, HasInputDevices, HasTouchScreen {

    private final WebDriver delegate;
    private final Set<WebDriverEventListener> listeners = new HashSet<>();

    public WebDriverDecorator(WebDriver driver) {
        this.delegate = driver;
    }

    public void registerListener(WebDriverEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void get(String url) {
        listeners.forEach(l -> l.beforeNavigateTo(url, delegate));
        try {
            delegate.get(url);
            listeners.forEach(l -> l.afterNavigateTo(url, delegate));
        } catch (Exception e) {
            sendExceptionToListeners(e);
        }
    }

    private void sendExceptionToListeners(Exception e) {
        listeners.forEach(l -> l.onException(e, delegate));
    }

    @Override
    public String getCurrentUrl() {
        return delegate.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return delegate.getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        listeners.forEach(l -> l.beforeFindBy(by, null, delegate));
        try {
            final List<WebElement> result = delegate.findElements(by);
            listeners.forEach(l -> l.afterFindBy(by, null, delegate));
            return result.stream().map(element -> new WebElementDecorator(element, delegate, by)).collect(toList());
        } catch (Exception e) {
            sendExceptionToListeners(e);
        }
        return emptyList();
    }

    @Override
    public WebElement findElement(By by) {
        listeners.forEach(l -> l.beforeFindBy(by, null, delegate));
        try {
            WebElement result = delegate.findElement(by);
            listeners.forEach(l -> l.afterFindBy(by, result, delegate));
            return new WebElementDecorator(result, delegate, by);
        } catch (Exception e) {
            sendExceptionToListeners(e);
            throw e;
        }
    }

    @Override
    public String getPageSource() {
        return delegate.getPageSource();
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public void quit() {
        delegate.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return delegate.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return delegate.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return delegate.switchTo();
    }

    @Override
    public Navigation navigate() {
        return new Navigation() {
            @Override
            public void back() {
                listeners.forEach(l -> l.beforeNavigateBack(delegate));
                delegate.navigate().back();
                listeners.forEach(l -> l.afterNavigateBack(delegate));
            }

            @Override
            public void forward() {
                listeners.forEach(l -> l.beforeNavigateForward(delegate));
                delegate.navigate().forward();
                listeners.forEach(l -> l.afterNavigateForward(delegate));

            }

            @Override
            public void to(String s) {
                listeners.forEach(l -> l.beforeNavigateTo(s, delegate));
                delegate.navigate().to(s);
                listeners.forEach(l -> l.afterNavigateTo(s, delegate));
            }

            @Override
            public void to(URL url) {
                listeners.forEach(l -> l.beforeNavigateTo(url.toString(), delegate));
                delegate.navigate().to(url);
                listeners.forEach(l -> l.afterNavigateTo(url.toString(), delegate));
            }

            @Override
            public void refresh() {
                listeners.forEach(l -> l.beforeNavigateRefresh(delegate));
                delegate.navigate().refresh();
                listeners.forEach(l -> l.beforeNavigateRefresh(delegate));
            }
        };
    }

    @Override
    public Options manage() {
        try {
            return delegate.manage();
        } catch (Exception e) {
            sendExceptionToListeners(e);
            return null;
        }
    }

    @Override
    public Object executeScript(String s, Object... objects) {
        listeners.forEach(l -> l.beforeScript(s, delegate));
        try {
            Object result = ((JavascriptExecutor) delegate).executeScript(s, objects);
            listeners.forEach(l -> l.afterScript(s, delegate));
            return result;
        } catch (Exception e) {
            sendExceptionToListeners(e);
            throw e;
        }
    }

    @Override
    public Object executeAsyncScript(String s, Object... objects) {
        listeners.forEach(l -> l.beforeScript(s, delegate));
        try {
            Object result = ((JavascriptExecutor) delegate).executeAsyncScript(s, objects);
            listeners.forEach(l -> l.afterScript(s, delegate));
            return result;
        } catch (Exception e) {
            sendExceptionToListeners(e);
            throw e;
        }
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return ((TakesScreenshot) delegate).getScreenshotAs(outputType);
    }

    @Override
    public Keyboard getKeyboard() {
        return ((HasInputDevices) delegate).getKeyboard();
    }

    @Override
    public Mouse getMouse() {
        return ((HasInputDevices) delegate).getMouse();
    }

    @Override
    public TouchScreen getTouch() {
        return ((HasTouchScreen) delegate).getTouch();
    }

    @Override
    public WebDriver getWrappedDriver() {
        return delegate;
    }

    @Override
    public String toString() {
        return format("%s wrapping a %s", getClass().getSimpleName(), delegate.toString());
    }
}
