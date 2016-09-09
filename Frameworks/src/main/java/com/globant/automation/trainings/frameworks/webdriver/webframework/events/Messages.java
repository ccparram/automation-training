package com.globant.automation.trainings.frameworks.webdriver.webframework.events;

import com.globant.automation.trainings.frameworks.webdriver.webframework.pageobject.PageObject;
import com.globant.automation.trainings.frameworks.webdriver.webframework.webdriver.Browser;

/**
 * @author Juan Krzemien
 */
public class Messages {

    private Messages() {
    }

    public static class PageObjects {

        public static <T extends PageObject> IPageObjectCreatedEvent CREATED(T pageObject) {
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
