package com.globant.automation.trainings.frameworks.webdriver.test.cucumber;


import com.globant.automation.trainings.frameworks.webdriver.test.pageobject.PageObject;

import java.lang.reflect.ParameterizedType;

import static com.globant.automation.trainings.frameworks.webdriver.test.cucumber.Context.PAGE_TO_CONTEXT;


/**
 * Created by Juan Krzemien on 7/25/2016.
 */
public abstract class StepsFor<T extends PageObject> {

    private T currentPage;

    public StepsFor() {
        try {
            this.currentPage = getPageObjectType().newInstance();
            PAGE_TO_CONTEXT(currentPage);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Class<T> getPageObjectType() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    protected T getCurrentPage() {
        return currentPage;
    }
}
