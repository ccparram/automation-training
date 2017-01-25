package frameworks.web;

import com.globant.automation.trainings.utils.Reflection;
import com.globant.automation.trainings.webdriver.annotations.DeletesCookies;
import com.globant.automation.trainings.webdriver.annotations.Url;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

/**
 * Base class for modeling a Web Page using the Page Object pattern
 *
 * @author Juan Krzemien
 */
public class PageObject extends WebDriverOperations {

    protected PageObject() {

        getLogger().info(format("Creating new [%s] Page Object instance...", getClass().getSimpleName()));

        navigateIfDecorated();

        getLogger().info(format("[%s] Page Object instance created...", getClass().getSimpleName()));

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

    private void navigateIfDecorated() {
        String envUrl = System.getenv("SUT_URL");
        if (envUrl != null && !envUrl.isEmpty()) {
            getLogger().info(format("Environment variable SUT_URL present! Navigating to [%s]...", envUrl));
            goToUrl(envUrl);
        } else {
            Url urlMark = getClass().getAnnotation(Url.class);
            if (urlMark != null) {
                getLogger().info(format("Page Object [%s] is marked with @Url, navigating to [%s]...", getClass().getSimpleName(), urlMark.value()));
                goToUrl(urlMark.value());
            }
        }
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
        List<WebElement> reflectiveType = Collections.emptyList();
        return Reflection.getFieldValuesOfType(this, reflectiveType.getClass());
    }

}
