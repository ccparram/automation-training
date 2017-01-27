package com.globant.automation.trainings.webframework.webdriver;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webframework.events.EventBus;
import com.globant.automation.trainings.webframework.events.IPageObjectCreatedEvent;
import com.globant.automation.trainings.webframework.events.ITestFinishedEvent;
import com.globant.automation.trainings.webframework.events.ITestStartedEvent;
import com.globant.automation.trainings.webframework.pageobject.PageObject;
import net.engio.mbassy.listener.Handler;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public enum DriverEventsHandler implements Logging {

    INSTANCE;

    public void init() {
        EventBus.FRAMEWORK.suscribe(this);
    }

    @Handler
    private WebDriver createBrowser(ITestStartedEvent event) throws MalformedURLException {
        getLogger().info(format("Creating a [%s] browser instance...", event.getBrowser().name()));
        return Drivers.INSTANCE.create(event.getBrowser());
    }

    @Handler
    private void injectDriverInPageObject(IPageObjectCreatedEvent event) {
        PageObject po = event.getPageObject();
        getLogger().info(format("Injecting WebDriver instance into Page Object [%s]...", po.getClass().getSimpleName()));
        WebDriver driver = Drivers.INSTANCE.get();
        po.setDriver(driver);
    }

    @Handler
    private void destroyBrowser(ITestFinishedEvent event) {
        getLogger().info(format("Destroying [%s] browser instance...", event.getBrowser().name()));
        Drivers.INSTANCE.destroy();
    }

}
