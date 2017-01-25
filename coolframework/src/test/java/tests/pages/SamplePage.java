package tests.pages;

import frameworks.web.PageObject;
import frameworks.web.annotations.Url;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Juan Krzemien
 */
@Url("http://www.nyaa.se/?cats=1_37&filter=2")
public class SamplePage extends PageObject {

    @FindBy(name = "term")
    private WebElement searchCriteria;

    @FindBy(className = "inputsearchsubmit")
    private WebElement searchButton;

}
