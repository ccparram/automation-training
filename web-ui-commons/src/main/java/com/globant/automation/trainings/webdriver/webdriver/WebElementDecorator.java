package com.globant.automation.trainings.webdriver.webdriver;

import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * @author Juan Krzemien
 */
public class WebElementDecorator implements WebElement, WrapsElement {

    private final WebElement delegate;
    private final WebDriver driver;

    private final Set<WebDriverEventListener> listeners = new HashSet<>();

    public WebElementDecorator(WebElement element, WebDriver driver) {
        this.delegate = element;
        this.driver = driver;
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
            return result.stream().map(element -> new WebElementDecorator(element, driver)).collect(toList());
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
            return new WebElementDecorator(result, driver);
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
        return delegate;
    }

    @Override
    public String toString() {
        String element = delegate.toString();
        // Leave just locator part
        return element.substring(element.indexOf("->") + 3, element.length() - 1);
    }
}