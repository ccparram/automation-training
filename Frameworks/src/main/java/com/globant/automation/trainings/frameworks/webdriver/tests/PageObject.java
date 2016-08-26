package com.globant.automation.trainings.frameworks.webdriver.tests;

import com.globant.automation.trainings.frameworks.webdriver.tests.annotations.DeletesCookies;
import com.globant.automation.trainings.frameworks.webdriver.tests.annotations.FocusFrames;
import com.globant.automation.trainings.frameworks.webdriver.tests.annotations.Url;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.openqa.selenium.support.ui.ExpectedConditions.frameToBeAvailableAndSwitchToIt;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

/**
 * Base class for modeling a Web Page using the Page Object pattern
 *
 * @author Juan Krzemien
 */
public abstract class PageObject extends PageCommon {

    public PageObject() {
        LOG.info(format("Creating new [%s] Page Object instance...", getClass().getSimpleName()));

        navigateIfDecorated();

        // This would be awesome to have, but reduces test speed dramatically!
        //assertTrue(isVisible(), format("One or more elements were not visible when initializing Page Object [%s]!", getClass().getSimpleName()));

        focusFrameIfDecorated();

        LOG.info(format("[%s] Page Object instance created...", getClass().getSimpleName()));

    }

    private void focusFrameIfDecorated() {
        FocusFrames focusFrames = getClass().getAnnotation(FocusFrames.class);
        if (focusFrames != null) {
            String framePath = stream(focusFrames.value()).collect(joining(" -> "));
            LOG.info(format("Page Object [%s] is marked with @FocusFrames, switching frame focus: Default Context -> %s", getClass().getSimpleName(), framePath));
            switchTo().defaultContent();
            for (String frame : focusFrames.value()) {
                waitFor(frameToBeAvailableAndSwitchToIt(frame));
            }
        } else {
            LOG.info(format("Switch to default content for Page Object [%s]...", getClass().getSimpleName()));
            switchTo().defaultContent();
        }
    }

    @Override
    public String toString() {
        return getClass().getName();
    }

    private void deleteCookiesIfMarked() {
        DeletesCookies deletesCookies = getClass().getAnnotation(DeletesCookies.class);
        if (deletesCookies != null) {
            LOG.info(format("Page Object [%s] is marked with @DeletesCookies, deleting...", getClass().getSimpleName()));
            driver.manage().deleteAllCookies(); // NOTE: IE sucks at deleting cookies (can't delete httpOnly ones)
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
        driver.get(url);
    }

    public void refresh() {
        driver.navigate().refresh();
        switchTo().defaultContent();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean isVisible() {
        waitFor(visibilityOfAllElements(getOwnWebElements()));
        return true;
    }
}
