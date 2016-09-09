package com.globant.automation.trainings.frameworks.webdriver.webframework.pages;

import com.globant.automation.trainings.frameworks.webdriver.webframework.pageobject.PageObject;
import com.globant.automation.trainings.frameworks.webdriver.webframework.pageobject.annotations.Url;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

/**
 * Sample Page Object for this particular test...
 *
 * @author Juan Krzemien
 */
@Url("http://www.google.com")
public class GoogleHomePage extends PageObject {

    @FindBy(id = "lst-ib")
    private WebElement searchBox;

    public GoogleSearchResults search(String criteria) {
        waitFor(elementToBeClickable(searchBox));
        searchBox.sendKeys(criteria + Keys.ENTER);
        return new GoogleSearchResults();
    }
}
