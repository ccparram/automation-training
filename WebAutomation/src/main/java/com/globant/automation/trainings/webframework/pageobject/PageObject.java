package com.globant.automation.trainings.webframework.pageobject;

import com.globant.automation.trainings.webframework.events.Messages;
import com.globant.automation.trainings.webframework.pageobject.annotations.DeletesCookies;
import com.globant.automation.trainings.webframework.pageobject.annotations.FocusFrames;
import com.globant.automation.trainings.webframework.pageobject.annotations.Url;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.globant.automation.trainings.webframework.events.EventBus.FRAMEWORK;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.openqa.selenium.support.ui.ExpectedConditions.frameToBeAvailableAndSwitchToIt;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

/**
 * Base class for modeling a Web Page using the Page Object pattern
 *
 * @author Juan Krzemien
 */
public class PageObject extends PageCommon {

    protected PageObject() {

        FRAMEWORK.post(Messages.PageObjects.CREATED(this));

        getLogger().info(format("Creating new [%s] Page Object instance...", getClass().getSimpleName()));

        navigateIfDecorated();

        focusFrameIfDecorated();

        getLogger().info(format("[%s] Page Object instance created...", getClass().getSimpleName()));

    }

    /**
     * If POM is marked with {@link FocusFrames}, processes annotation
     * values and switches WebDriver focus to the frames mentioned in the list, from first to last.
     */
    private void focusFrameIfDecorated() {
        FocusFrames focusFrames = getClass().getAnnotation(FocusFrames.class);
        if (focusFrames != null) {
            String framePath = stream(focusFrames.value()).collect(joining(" -> "));
            switchTo().defaultContent();
            getLogger().info(format("Page Object [%s] is marked with @FocusFrames, switching frame focus: Default Context -> %s", getClass().getSimpleName(), framePath));
            for (String frame : focusFrames.value()) {
                waitFor(frameToBeAvailableAndSwitchToIt(frame));
            }
        }
        /*else {
            getLogger().info(format("Switch to default content for Page Object [%s]...", getClass().getSimpleName()));
            switchTo().defaultContent();
        }*/
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
        getOwnWebElements().forEach(ExpectedConditions::visibilityOf);
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

}
