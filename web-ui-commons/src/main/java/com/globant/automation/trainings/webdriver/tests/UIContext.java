package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.languages.Language;
import com.globant.automation.trainings.tests.Context;
import com.globant.automation.trainings.tests.DefaultContext;
import com.globant.automation.trainings.tests.TestContext;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.webdriver.WebDriverDecorator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.function.Supplier;

import static com.globant.automation.trainings.utils.Lazy.lazily;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.openqa.selenium.OutputType.BASE64;
import static org.openqa.selenium.OutputType.FILE;

/**
 * Context information holder for tests during their run time / life cycle.
 *
 * @author Juan Krzemien
 */
public class UIContext extends DefaultContext {

    private static final WebDriverFactory WEB_DRIVER_FACTORY = new WebDriverFactory();
    private final Browser browser;
    private final Supplier<WebDriverDecorator> driver;

    private UIContext(Browser browser, Language language) {
        super(language);
        this.browser = browser;
        this.driver = lazily(this::initializeDriver);
    }

    public static Context with(Browser browser, Language language) {
        return new UIContext(browser, language);
    }

    public static synchronized String captureScreenShot() throws IOException {
        String base64ScreenShot = ((UIContext) TestContext.get()).getDriver().getScreenshotAs(BASE64);
        String dateTime = now().format(ofPattern("ddMMyyyy-hhmmss"));
        File screenShotFile = Paths.get(format("screenshots/screenShot%s.png", dateTime)).toFile();
        copyFile(FILE.convertFromBase64Png(base64ScreenShot), screenShotFile);
        return format("../%s", screenShotFile.getPath());
    }

    @Override
    public void init() {
        // Override behaviour
    }

    @Override
    public void destroy() {
        getDriver().quit();
    }

    private WebDriverDecorator initializeDriver() {
        try {
            return WEB_DRIVER_FACTORY.createFor(browser);
        } catch (MalformedURLException e) {
            getLogger().error("Could not create WebDriver instance!", e);
            return null;
        }
    }

    public Browser getBrowser() {
        return browser;
    }

    WebDriverDecorator getDriver() {
        return driver.get();
    }

}
