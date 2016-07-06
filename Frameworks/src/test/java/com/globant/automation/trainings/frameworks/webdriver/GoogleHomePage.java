package com.globant.automation.trainings.frameworks.webdriver;

import com.globant.automation.trainings.frameworks.webdriver.tests.PageObject;
import com.globant.automation.trainings.frameworks.webdriver.tests.Url;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

/**
 * Sample Page Object for this particular test...
 */
@Url("http://www.google.com")
public class GoogleHomePage extends PageObject {

    @FindBy(id = "lst-ib")
    private WebElement searchBox;

    public GoogleSearchResults search(String criteria) {
        wait.until(elementToBeClickable(searchBox));
        searchBox.sendKeys(criteria + Keys.ENTER);
        return new GoogleSearchResults();
    }
}
