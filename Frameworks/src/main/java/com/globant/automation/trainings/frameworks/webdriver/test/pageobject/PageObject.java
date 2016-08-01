package com.globant.automation.trainings.frameworks.webdriver.test.pageobject;

import com.globant.automation.trainings.frameworks.webdriver.annotations.DeletesCookies;
import com.globant.automation.trainings.frameworks.webdriver.annotations.Url;
import com.globant.automation.trainings.frameworks.webdriver.factories.Drivers;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import static com.globant.automation.trainings.frameworks.webdriver.test.cucumber.Context.PAGE_TO_CONTEXT;
import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class PageObject extends PageCommon {

    protected static final Logger LOG = getLogger(PageObject.class);

    public PageObject() {
        LOG.info(format("Creating new [%s] Page Object instance...", getClass().getSimpleName()));

        deleteCookies();
        navigate();

        PageFactory.initElements(driver, this);
        LOG.info(format("[%s] Page Object instance created...", getClass().getSimpleName()));

        PAGE_TO_CONTEXT(this);

        //assertTrue(isVisible(), format("Not at %s page", getClass().getSimpleName()));
    }

    @Override
    public String toString() {
        return getClass().getName();
    }

    private void deleteCookies() {
        DeletesCookies deletesCookies = getClass().getAnnotation(DeletesCookies.class);
        if (deletesCookies != null) {
            LOG.info(format("Page Object [%s] is marked with @DeletesCookies, deleting...", getClass().getSimpleName()));
            Drivers.INSTANCES.get().manage().deleteAllCookies();
        }
    }

    private void navigate() {
        String envUrl = System.getenv("SUT_URL");
        if (envUrl != null && !envUrl.isEmpty()) {
            LOG.info(format("Environment variable SUT_URL present! Navigating to [%s]...", envUrl));
            goToUrl(envUrl);
        } else {
            Url urlMark = getClass().getAnnotation(Url.class);
            if (urlMark != null) { // && !driver.getCurrentUrl().startsWith("http")
                LOG.info(format("Page Object [%s] is marked with @Url, navigating to [%s]...", getClass().getSimpleName(), urlMark.value()));
                goToUrl(urlMark.value());
            }
        }
    }

    protected void goToUrl(String url) {
        driver.get(url);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public abstract boolean isVisible();
}
