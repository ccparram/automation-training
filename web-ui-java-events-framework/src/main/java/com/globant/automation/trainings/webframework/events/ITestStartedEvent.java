package com.globant.automation.trainings.webframework.events;


import com.globant.automation.trainings.webdriver.browsers.Browser;

/**
 * @author Juan Krzemien
 */
public interface ITestStartedEvent extends IEvent {
    Browser getBrowser();
}
