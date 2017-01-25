package frameworks.web;

import org.openqa.selenium.WebDriver;

import java.util.Optional;

/**
 * @author Juan Krzemien
 */
enum WebDriverContext {

    WEB_DRIVER_CONTEXT;

    private static final ThreadLocal<WebDriver> DRIVERS_PER_THREAD = new ThreadLocal<>();

    public void set(WebDriver driver) {
        DRIVERS_PER_THREAD.set(driver);
    }

    public WebDriver get() {
        return DRIVERS_PER_THREAD.get();
    }

    public void remove() {
        Optional.ofNullable(get()).ifPresent(WebDriver::quit);
        DRIVERS_PER_THREAD.remove();
    }

}
