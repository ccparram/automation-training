package com.globant.automation.trainings.frameworks.webdriver.test.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PageElement extends PageCommon {

    private By contextLocator;

    private WebElement contextElement;

    public PageElement(By locator) {
        contextLocator = locator;
    }

    protected WebElement getContextElement() {
        if (contextElement == null) {
            contextElement = findElement(contextLocator);
        }
        return contextElement;
    }
}
