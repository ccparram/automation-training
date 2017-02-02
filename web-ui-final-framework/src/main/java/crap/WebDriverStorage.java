package crap;

import org.openqa.selenium.WebDriver;

/**
 * @author Juan Krzemien
 */
class WebDriverStorage {

    private static final ThreadLocal<WebDriver> DRIVERS_PER_THREAD = new ThreadLocal<>();

    static WebDriver get() {
        return DRIVERS_PER_THREAD.get();
    }

    static void remove() {
        WebDriver driver = DRIVERS_PER_THREAD.get();
        if (driver != null) {
            driver.quit();
        }
        DRIVERS_PER_THREAD.remove();
    }

    static void set(WebDriver driver) {
        DRIVERS_PER_THREAD.set(driver);
    }
}
