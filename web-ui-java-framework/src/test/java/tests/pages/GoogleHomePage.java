package tests.pages;

import com.globant.automation.trainings.webdriver.annotations.Url;
import com.globant.automation.trainings.webframework.PageObject;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Sample Page Object for this particular test...
 *
 * @author Juan Krzemien
 */
@Url("http://www.google.com")
public class GoogleHomePage extends PageObject<GoogleHomePage> {

    @FindBy(id = "lst-ib")
    private WebElement searchBox;

    public GoogleSearchResults search(Object criteria) {
        type(searchBox, criteria.toString() + Keys.ENTER);
        return new GoogleSearchResults();
    }
}
