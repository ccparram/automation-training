package com.globant.automation.trainings.pages;

import com.globant.automation.trainings.webdriver.annotations.Url;
import com.globant.automation.trainings.webdriver.tests.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Juan Krzemien
 */
@Url("http://www.nyaa.se/?cats=1_37&filter=2")
public class SamplePage extends PageObject<SamplePage> {

    @FindBy(name = "term")
    private WebElement searchCriteria;

    @FindBy(className = "inputsearchsubmit")
    private WebElement searchButton;

}
