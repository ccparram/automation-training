package com.globant.automation.trainings.webframework.events;

import com.globant.automation.trainings.webframework.pageobject.PageObject;

/**
 * @author Juan Krzemien
 */
public interface IPageObjectCreatedEvent extends IEvent {
    PageObject getPageObject();
}