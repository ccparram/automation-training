package com.globant.automation.trainings.frameworks.webdriver.webframework.events;

import com.globant.automation.trainings.frameworks.webdriver.webframework.webdriver.Browser;

/**
 * @author Juan Krzemien
 */
public interface ITestFinishedEvent extends IEvent {
    Browser getBrowser();
}
