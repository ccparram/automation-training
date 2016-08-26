package com.globant.automation.trainings.frameworks.webdriver.tests.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Juan Krzemien on 8/24/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FocusFrames {

    String[] value() default ""; // NOTE: ORDER IN WHICH YOU DEFINE THE FRAME IDs/NAMEs IS IMPORTANT!!!

}
