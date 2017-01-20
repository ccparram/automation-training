package frameworks.web;

import org.openqa.selenium.WebDriver;

import static frameworks.web.WebDriverContext.WEB_DRIVER_CONTEXT;

/**
 * @author Juan Krzemien
 */
public abstract class BasePageObject {

    protected abstract String getPageUrl();

    protected WebDriver getDriver() {
        return WEB_DRIVER_CONTEXT.get();
    }

    public void navigate() {
        getDriver().navigate().to(getPageUrl());
    }

}
