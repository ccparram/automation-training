package com.globant.automation.trainings.frameworks.webdriver.interfaces;

import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Created by jkrzemien on 25/07/2016.
 */
public interface Waitable {

    <T> T doWait(ExpectedCondition<T> condition);

}
