package com.globant.automation.trainings.webdriver.tests;

import com.globant.automation.trainings.utils.Reflection;
import com.globant.automation.trainings.webdriver.annotations.DeletesCookies;
import com.globant.automation.trainings.webdriver.annotations.Url;
import com.globant.automation.trainings.webdriver.waiting.conditions.Conditions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

/**
 * Base class for modeling a Web Page using the Page Object pattern
 *
 * @author Juan Krzemien
 */
public class PageObject<T extends PageObject> extends WebDriverOperations {

    public PageObject() {
        getLogger().info(format("Creating new [%s] Page Object instance...", getClass().getSimpleName()));
        deleteCookiesIfDecorated();
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
        final AtomicBoolean fromEnvironment = new AtomicBoolean(true);
        String url = Optional.ofNullable(System.getenv("SUT_URL")).orElseGet(() -> {
            fromEnvironment.set(false);
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
        getDriver().navigate().to(url);
    }

    public void refresh() {
        getDriver().navigate().refresh();
        switchTo().defaultContent();
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }

    public boolean isVisible() {
        getOwnWebElements().forEach(e -> {
            if (e instanceof WebElement) {
                waitUntil((WebElement) e).is(Conditions.Element.Visibility.Visible);
            } else {
                List<WebElement> le = (List<WebElement>) e;
                waitUntil(le).are(Conditions.Elements.Visibility.Visible);
            }
        });
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

    protected List<?> getOwnWebElements() {
        return Reflection.getFieldValuesAnnotatedWith(this, FindBy.class);
    }

}
