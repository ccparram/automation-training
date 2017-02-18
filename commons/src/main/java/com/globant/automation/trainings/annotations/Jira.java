package com.globant.automation.trainings.annotations;

import java.lang.annotation.*;

/**
 * Marker annotation for future JIRA integration
 *
 * @author Juan Krzemien
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Jiras.class)
public @interface Jira {
    String id();

    String name() default "";
}
