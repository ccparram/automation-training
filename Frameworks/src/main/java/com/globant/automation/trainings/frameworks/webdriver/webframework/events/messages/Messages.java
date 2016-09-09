package com.globant.automation.trainings.frameworks.webdriver.webframework.events.messages;

import com.globant.automation.trainings.frameworks.webdriver.webframework.events.messages.interfaces.IPageObjectCreatedEvent;
import com.globant.automation.trainings.frameworks.webdriver.webframework.events.messages.interfaces.ITestFinishedEvent;
import com.globant.automation.trainings.frameworks.webdriver.webframework.events.messages.interfaces.ITestStartedEvent;
import com.globant.automation.trainings.frameworks.webdriver.webframework.webdriver.Browser;

/**
 * @author Juan Krzemien
 */
public class Messages {

    public static class PageObject {

        public static <T extends com.globant.automation.trainings.frameworks.webdriver.webframework.pageobject.PageObject> IPageObjectCreatedEvent CREATED(T pageObject) {
            return () -> pageObject;
        }

    }

    public static class Test {

        public static ITestStartedEvent START(Browser browser) {
            return () -> browser;
        }

        public static ITestFinishedEvent FINISH(Browser browser) {
            return () -> browser;
        }

    }

}
