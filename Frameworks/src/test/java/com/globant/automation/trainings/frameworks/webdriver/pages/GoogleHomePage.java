package com.globant.automation.trainings.frameworks.webdriver.pages;

import com.globant.automation.trainings.frameworks.webdriver.annotations.Url;
import com.globant.automation.trainings.frameworks.webdriver.test.pageobject.PageObject;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

/**
 * Sample Page Object for Google's Home Page
 *
 * @author Juan Krzemien
 */
@Url("http://www.google.com")
public class GoogleHomePage extends PageObject {

    @FindBy(id = "lst-ib")
    private WebElement searchBox;

    public GoogleSearchResults search(String criteria) {
        doWait(elementToBeClickable(searchBox));
        searchBox.sendKeys(criteria + Keys.ENTER);
        return new GoogleSearchResults();
    }

    @Override
    public boolean isVisible() {
        return doWait(visibilityOf(searchBox)).isDisplayed();
    }
}
