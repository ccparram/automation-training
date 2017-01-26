package frameworks.web;

import com.globant.automation.trainings.utils.Reflection;
import com.globant.automation.trainings.webdriver.annotations.DeletesCookies;
import com.globant.automation.trainings.webdriver.annotations.Url;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

/**
 * Base class for modeling a Web Page using the Page Object pattern
 *
 * @author Juan Krzemien
 */
public class PageObject<T extends PageObject> extends WebDriverOperations {

    protected PageObject() {
        getLogger().info(format("Creating new [%s] Page Object instance...", getClass().getSimpleName()));
    }


    /**
     * Pretty print POM name, when requested as a String
     *
     * @return the class name of the POM
     */
    @Override
    public String toString() {
        return getClass().getName();
    }

    private void deleteCookiesIfDecorated() {
        DeletesCookies deletesCookies = getClass().getAnnotation(DeletesCookies.class);
        if (deletesCookies != null) {
            getLogger().info(format("Page Object [%s] is marked with @DeletesCookies, deleting...", getClass().getSimpleName()));
            getDriver().manage().deleteAllCookies();
        }
    }

    public T open() {
        final AtomicBoolean fromEnvironment = new AtomicBoolean(false);
        String url = Optional.ofNullable(System.getenv("SUT_URL")).orElseGet(() -> {
            fromEnvironment.set(true);
            Url urlMark = Optional.ofNullable(getClass().getAnnotation(Url.class)).orElseThrow(() -> new IllegalArgumentException("@Url is not present in this Page Object"));
            getLogger().info(format("Page Object [%s] is marked with @Url, navigating to [%s]...", getClass().getSimpleName(), urlMark.value()));
            return urlMark.value();
        });
        if (fromEnvironment.get()) {
            getLogger().info(format("Environment variable SUT_URL present! Navigating to [%s]...", url));
        }
        goToUrl(url);
        return (T) this;
    }

    protected void goToUrl(String url) {
        deleteCookiesIfDecorated();
        getDriver().get(url);
    }

    public void refresh() {
        getDriver().navigate().refresh();
        switchTo().defaultContent();
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }

    public boolean isVisible() {
        getOwnWebElements().forEach(e -> waitFor(visibilityOf(e)));
        return true;
    }

    protected void switchWindows() {
        waitFor(numberOfWindowsToBe(2));
        String currentWindow = getDriver().getWindowHandle();
        Set<String> openWindows = new LinkedHashSet<>(getDriver().getWindowHandles());
        for (String openWindow : openWindows) {
            if (!openWindow.equals(currentWindow)) {
                switchTo().window(openWindow);
                break;
            }
        }
    }

    protected List<WebElement> getOwnWebElements() {
        return Reflection.getFieldValuesAnnotatedWith(this, FindBy.class);
    }

}
