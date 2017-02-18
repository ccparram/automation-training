package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.languages.Language;
import com.globant.automation.trainings.webdriver.webdriver.WebDriverDecorator;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.util.function.Supplier;

import static com.globant.automation.trainings.utils.Lazy.lazily;
import static java.util.Optional.ofNullable;

/**
 * @author Juan Krzemien
 */
public class TestContext {

    private static final ThreadLocal<Context> CONTEXT_PER_THREAD = new ThreadLocal<>();

    public static Context get() {
        return CONTEXT_PER_THREAD.get();
    }

    public static void set(Context context) {
        CONTEXT_PER_THREAD.set(context);
    }

    public static void remove() {
        ofNullable(get()).ifPresent(context -> ofNullable(context.getDriver()).ifPresent(WebDriver::quit));
        CONTEXT_PER_THREAD.remove();
    }

    public static Context with(Browser browser, Language language) {
        return new Context(browser, language);
    }

    public static class Context implements Logging {

        private static final WebDriverFactory WEB_DRIVER_FACTORY = new WebDriverFactory();

        private final Browser browser;
        private final Language language;
        private final Supplier<WebDriverDecorator> driver;

        private Context(Browser browser, Language language) {
            this.browser = browser;
            this.language = language;
            this.driver = lazily(() -> {
                try {
                    return WEB_DRIVER_FACTORY.createFor(browser);
                } catch (MalformedURLException e) {
                    getLogger().error("Could not create WebDriver instance!", e);
                    return null;
                }
            });
        }

        public Browser getBrowser() {
            return browser;
        }

        public Language getLanguage() {
            return language;
        }

        WebDriverDecorator getDriver() {
            return driver.get();
        }

    }

}
