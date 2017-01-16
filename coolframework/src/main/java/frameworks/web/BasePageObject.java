package frameworks.web;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Juan Krzemien
 */
public abstract class BasePageObject {

    @Autowired
    private WebDriverProvider webDriverProvider;

    protected abstract String getPageUrl();

    protected WebDriver getDriver() {
        return webDriverProvider.get();
    }

    public void navigate() {
        getDriver().navigate().to(getPageUrl());
    }

    public void dispose() {
        webDriverProvider.dispose();
    }
}
