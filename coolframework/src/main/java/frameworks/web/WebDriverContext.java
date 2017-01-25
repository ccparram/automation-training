package frameworks.web;

import frameworks.logging.Logging;
import org.openqa.selenium.WebDriver;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

/**
 * Storage for *all* WebDriver instances for every thread.
 *
 * @author Juan Krzemien
 */
enum WebDriverContext implements Logging {

    WEB_DRIVER_CONTEXT;

    private static final AtomicInteger DRIVER_INSTANCES = new AtomicInteger();
    private static final ThreadLocal<WebDriver> DRIVERS_PER_THREAD = new ThreadLocal<>();

    public void set(WebDriver driver) {
        getLogger().info(format("Active WebDriver instances: %d", DRIVER_INSTANCES.incrementAndGet()));
        DRIVERS_PER_THREAD.set(driver);
    }

    public WebDriver get() {
        return DRIVERS_PER_THREAD.get();
    }

    public void remove() {
        Optional.ofNullable(get()).ifPresent(d -> {
            getLogger().info(format("Active WebDriver instances: %d", DRIVER_INSTANCES.decrementAndGet()));
            d.quit();
        });
        DRIVERS_PER_THREAD.remove();
    }

}
