package com.globant.automation.trainings.pages;

import com.globant.automation.trainings.webdriver.annotations.Url;
import com.globant.automation.trainings.webdriver.tests.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Juan Krzemien
 */
@Url("/?cats=1_37&filter=2")
public class SamplePage extends PageObject<SamplePage> {

    @FindBy(name = "term")
    private WebElement searchCriteria;

    @FindBy(className = "inputsearchsubmit")
    private WebElement searchButton;

    public boolean isVisible() {
        return isVisible(searchCriteria) && isVisible(searchButton);
    }

    public SamplePage doSearch(String criteria) {
        type(searchCriteria, criteria);
        click(searchButton);
        return new SamplePage();
    }
}
