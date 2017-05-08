package com.globant.automation.trainings.pages;

import com.globant.automation.trainings.webdriver.annotations.Url;
import com.globant.automation.trainings.webdriver.tests.PageObject;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Sample Page Object for this particular test...
 *
 * @author Juan Krzemien
 */
@Url("http://www.google.com")
public class GoogleHome extends PageObject<GoogleHome> {

    @FindBy(id = "lst-ib")
    private WebElement searchBox;

    public GoogleSearchResults search(Object criteria) {
        type(searchBox, criteria.toString() + Keys.ENTER);
        return new GoogleSearchResults();
    }

    public boolean isVisible() {
        return isVisible(searchBox);
    }
}
