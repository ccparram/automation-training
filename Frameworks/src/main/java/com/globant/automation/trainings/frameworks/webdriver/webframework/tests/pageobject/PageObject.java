package com.globant.automation.trainings.frameworks.webdriver.webframework.tests.pageobject;

import com.globant.automation.trainings.frameworks.webdriver.webframework.tests.pageobject.annotations.DeletesCookies;
import com.globant.automation.trainings.frameworks.webdriver.webframework.tests.pageobject.annotations.FocusFrames;
import com.globant.automation.trainings.frameworks.webdriver.webframework.tests.pageobject.annotations.Url;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.LinkedHashSet;
import java.util.Set;

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
public abstract class PageObject extends PageCommon {

    public PageObject() {
        LOG.info(format("Creating new [%s] Page Object instance...", getClass().getSimpleName()));

        navigateIfDecorated();

        LOG.info(format("[%s] Page Object instance created...", getClass().getSimpleName()));

        focusFrameIfDecorated();
    }

    /**
     * If POM is marked with {@link FocusFrames}, processes annotation
     * values and switches WebDriver focus to the frames mentioned in the list, from first to last.
     */
    protected void focusFrameIfDecorated() {
        FocusFrames focusFrames = getClass().getAnnotation(FocusFrames.class);
        if (focusFrames != null) {
            String framePath = stream(focusFrames.value()).collect(joining(" -> "));
            switchTo().defaultContent();
            LOG.info(format("Page Object [%s] is marked with @FocusFrames, switching frame focus: Default Context -> %s", getClass().getSimpleName(), framePath));
            for (String frame : focusFrames.value()) {
                waitFor(frameToBeAvailableAndSwitchToIt(frame));
            }
        }
        /*else {
            LOG.info(format("Switch to default content for Page Object [%s]...", getClass().getSimpleName()));
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

    private void deleteCookiesIfMarked() {
        DeletesCookies deletesCookies = getClass().getAnnotation(DeletesCookies.class);
        if (deletesCookies != null) {
            LOG.info(format("Page Object [%s] is marked with @DeletesCookies, deleting...", getClass().getSimpleName()));
            getDriver().manage().deleteAllCookies();
        }
    }

    private void navigateIfDecorated() {
        String envUrl = System.getenv("SUT_URL");
        if (envUrl != null && !envUrl.isEmpty()) {
            LOG.info(format("Environment variable SUT_URL present! Navigating to [%s]...", envUrl));
            goToUrl(envUrl);
        } else {
            Url urlMark = getClass().getAnnotation(Url.class);
            if (urlMark != null) {
                LOG.info(format("Page Object [%s] is marked with @Url, navigating to [%s]...", getClass().getSimpleName(), urlMark.value()));
                goToUrl(urlMark.value());
            }
        }
    }

    protected void goToUrl(String url) {
        deleteCookiesIfMarked();
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
