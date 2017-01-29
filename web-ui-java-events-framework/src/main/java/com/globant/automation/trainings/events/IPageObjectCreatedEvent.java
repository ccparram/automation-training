package com.globant.automation.trainings.events;

import com.globant.automation.trainings.pageobject.PageObject;

/**
 * @author Juan Krzemien
 */
public interface IPageObjectCreatedEvent extends IEvent {
    PageObject getPageObject();
}