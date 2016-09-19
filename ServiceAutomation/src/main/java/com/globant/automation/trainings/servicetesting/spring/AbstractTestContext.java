package com.globant.automation.trainings.servicetesting.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Juan Krzemien
 */
@Configuration
@Import(Properties.class)
public abstract class AbstractTestContext {

}
