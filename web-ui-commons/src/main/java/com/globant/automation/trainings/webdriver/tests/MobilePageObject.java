package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.tests.TestContext;
import com.globant.automation.trainings.webdriver.webdriver.WebDriverDecorator;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import static com.globant.automation.trainings.logging.Reporter.REPORTER;
import static com.globant.automation.trainings.utils.Reflection.getFieldValuesAnnotatedWith;
import static com.globant.automation.trainings.webdriver.browsers.Browser.ANDROID;
import static java.lang.String.format;

/**
 * Base class for modeling a Web Page using the Page Object pattern
 *
 * @author Juan Krzemien
 */
public class MobilePageObject<T extends MobilePageObject> extends PageCommon {

    public MobilePageObject() {
        getLogger().info(format("Creating new [%s] Mobile Page Object instance...", getClass().getSimpleName()));
    }

    @Override
    protected void initializePageObject(WebDriverDecorator driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver.getWrappedDriver()), this);
    }

    private TouchAction getTouchAction() {
        return new TouchAction((PerformsTouchActions) getDriver().getWrappedDriver());
    }

    private MultiTouchAction getMultiTouchAction() {
        return new MultiTouchAction((PerformsTouchActions) getDriver().getWrappedDriver());
    }

    protected MobilePageObject tap(int x, int y) {
        getTouchAction().tap(x, y).perform();
        return this;
    }

    protected MobilePageObject doubleTap(int x, int y) {
        TouchAction tap = getTouchAction().tap(x, y);
        getMultiTouchAction()
                .add(tap)
                .add(tap)
                .perform();
        return this;
    }

    private void switchToWebView() {
        AppiumDriver driver = (AppiumDriver) getDriver().getWrappedDriver();
        if (ANDROID.equals(((UIContext) TestContext.get()).getBrowser())) {
            driver.context("WEBVIEW_" + driver.getCapabilities().getCapability("androidPackage"));
        }
    }

    /**
     * Navigates to a given URL as a string
     *
     * @param url Website's URL
     */
    protected void goToUrl(String url) {
        switchToWebView();
        REPORTER.info("Navigating to %s", url);
        getDriver().navigate().to(url);
    }

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
