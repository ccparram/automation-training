package com.globant.automation.trainings.frameworks.webdriver.test.pageobject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Juan Krzemien on 7/26/2016.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetterForField {
    String value();
}
