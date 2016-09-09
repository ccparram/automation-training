package com.globant.automation.trainings.frameworks.webdriver.webframework.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

/**
 * Public abstract class for modeling a *portion* (or *section*) of a Web Page using the Page Object pattern.
 *
 * @author Juan Krzemien
 */
public class PageElement extends PageCommon {

    private final By ctxLocator;
    private WebElement ctxElement;

    protected PageElement(By contextLocator) {
        this.ctxLocator = contextLocator;
    }

    protected WebElement getContextElement() {
        if (ctxElement == null) {
            this.ctxElement = waitFor(visibilityOfElementLocated(ctxLocator));
        }
        return ctxElement;
    }

    protected By getContextLocator() {
        return ctxLocator;
    }

}
