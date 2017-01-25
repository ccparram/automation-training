package com.globant.automation.trainings.webdriver.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks POM classes to automatically switch to one (or more) inner Iframes in the HTML before WebDriver operates on the POM instance.
 * <p>
 * <b>NOTE</b> <i>ORDER IN WHICH YOU DEFINE THE FRAME IDs/NAMEs IS IMPORTANT!!!</i>
 *
 * @author Juan Krzemien
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FocusFrames {

    /**
     * <p>
     * <b>NOTE</b> <i>ORDER IN WHICH YOU DEFINE THE FRAME IDs/NAMEs IS IMPORTANT!!!</i>
     *
     * @return A list of strings representing IFrame names or ids to switch to.
     */
    String[] value() default "";

}
