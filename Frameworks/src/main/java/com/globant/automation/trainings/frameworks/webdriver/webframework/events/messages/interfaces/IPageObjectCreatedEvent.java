package com.globant.automation.trainings.frameworks.webdriver.webframework.events.messages.interfaces;

import com.globant.automation.trainings.frameworks.webdriver.webframework.pageobject.PageObject;

/**
 * @author Juan Krzemien
 */
public interface IPageObjectCreatedEvent extends IEvent {
    PageObject getPageObject();
}