package com.globant.automation.trainings.webdriver.tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import static com.globant.automation.trainings.utils.Reflection.getFieldValuesAnnotatedWith;
import static com.globant.automation.trainings.webdriver.browsers.Browser.ANDROID;
import static java.lang.String.format;

/**
 * Base class for modeling a Web Page using the Page Object pattern
 *
 * @author Juan Krzemien
 */
public class MobilePageObject<T extends MobilePageObject> extends PageObject<T> {

    public MobilePageObject() {
        getLogger().info(format("Creating new [%s] Mobile Page Object instance...", getClass().getSimpleName()));
    }

    @Override
    protected void initializePageObject() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    private void switchToWebView() {
        AppiumDriver driver = (AppiumDriver) getDriver();
        if (ANDROID.equals(getBrowser())) {
            driver.context("WEBVIEW_" + driver.getCapabilities().getCapability("androidPackage"));
        }
    }

    @Override
    protected void goToUrl(String url) {
        switchToWebView();
        super.goToUrl(url);
    }

    @Override
    protected List<?> getOwnWebElements() {
        List<?> web = getFieldValuesAnnotatedWith(this, FindBy.class);
        List<?> android = getFieldValuesAnnotatedWith(this, AndroidFindBy.class);
        List<?> ios = getFieldValuesAnnotatedWith(this, iOSFindBy.class);
        List<Object> all = new ArrayList<>();
        all.addAll(web);
        all.addAll(android);
        all.addAll(ios);
        return all;
    }

}
