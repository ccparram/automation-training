package com.globant.automation.trainings.frameworks.webdriver.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

/**
 * Base class for modeling a *portion* or *section* of a Web Page using the Page Object pattern
 *
 * @author Juan Krzemien
 */
public class PageElement extends PageCommon {

    private final WebElement contextElement;
    private By contextLocator;

    public PageElement(WebElement contextElement) {
        this.contextElement = contextElement;
    }

    public PageElement(By contextLocator) {
        this.contextLocator = contextLocator;
        this.contextElement = waitFor(visibilityOfElementLocated(contextLocator));
    }

    protected WebElement getContextElement() {
        waitFor(not(stalenessOf(contextElement)));
        return contextElement;
    }

    protected By getContextLocator() {
        return contextLocator;
    }

}
