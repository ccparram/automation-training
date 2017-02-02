package com.globant.automation.trainings.locators;

import org.openqa.selenium.By;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public class DynamicLocators {

    public static class Home {

        // Simulates a locator for item not present in DOM until you click on a button
        public static final By DynamicItem = new By.ByCssSelector(".menu.selected-item");
    }

    public static class Login {

        // Simulates a locator for a message that pops up after login. Varies the text depending on who is logging in.
        public static By WelcomeMessage(String user) {
            return new By.ByLinkText(format("Welcome back %s!", user));
        }
    }
}
