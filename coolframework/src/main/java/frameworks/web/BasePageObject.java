package frameworks.web;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Juan Krzemien
 */
public abstract class BasePageObject {

    @Autowired
    private WebDriver webDriver;

    protected abstract String getPageUrl();

    protected WebDriver getDriver() {
        return webDriver;
    }

    public void navigate() {
        getDriver().navigate().to(getPageUrl());
    }

    public void dispose() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
