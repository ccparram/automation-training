package com.globant.automation.trainings.tests.junit;

import org.junit.runners.Parameterized;

/**
 * This class is supposed to be used from JUnit's RunWith annotation.
 * Adds support for parallel test runs (default is double the number of available processors).
 * Extends JUnit's Parameterized class, so you still get the parametrized tests :)
 *
 * @author Juan Krzemien
 */
public class ParametrizedParallelism extends Parameterized {

    public ParametrizedParallelism(Class<?> clazz) throws Throwable {
        super(clazz);
        setScheduler(new ThreadPoolScheduler());
    }
}
