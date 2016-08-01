package com.globant.automation.trainings.frameworks.webdriver.test.pageobject;

import com.globant.automation.trainings.frameworks.webdriver.factories.Drivers;
import com.globant.automation.trainings.frameworks.webdriver.interfaces.Waitable;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static com.globant.automation.trainings.frameworks.webdriver.config.Framework.CONFIGURATION;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

class PageCommon implements Waitable {

    final WebDriver driver = Drivers.INSTANCES.get();

    private final WebDriverWait wait;

    public PageCommon() {
        wait = new WebDriverWait(driver, CONFIGURATION.WebDriver().getExplicitTimeOut());
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        wait.pollingEvery(CONFIGURATION.WebDriver().getPollingEveryMs(), MILLISECONDS);
    }

    @Override
    public <T> T doWait(ExpectedCondition<T> condition) {
        return wait.until(condition);
    }

    protected WebDriver.TargetLocator switchTo() {
        return driver.switchTo();
    }

    protected Actions getActions() {
        return new Actions(driver);
    }

    protected void type(WebElement element, String text) {
        doWait(visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    protected void select(WebElement element, String option) {
        doWait(visibilityOf(element));
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

    protected void click(WebElement element) {
        doWait(elementToBeClickable(element));
        element.click();
    }

    protected String getValue(WebElement element) {
        doWait(visibilityOf(element));
        return element.getAttribute("value");
    }

    protected String getSelectedOption(WebElement element) {
        doWait(visibilityOf(element));
        return new Select(element).getFirstSelectedOption().getText();
    }

    protected List<String> getSelectedOptions(WebElement element) {
        doWait(visibilityOf(element));
        return new Select(element).getAllSelectedOptions()
                .stream().map(WebElement::getText).collect(toList());
    }

    protected final WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    protected final List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }
}
