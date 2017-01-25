package com.globant.automation.trainings.webframework.events;


import com.globant.automation.trainings.webdriver.browsers.Browser;

/**
 * @author Juan Krzemien
 */
public interface ITestFinishedEvent extends IEvent {
    Browser getBrowser();
}
