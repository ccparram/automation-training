package com.globant.automation.trainings.runners.junit;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;

import static java.lang.String.format;
import static java.lang.System.err;
import static java.lang.Thread.currentThread;

/**
 * This class is supposed to be used from JUnit's RunWith annotation.
 * Adds support for parallel test runs (default is double the number of available processors).
 *
 * @author Juan Krzemien
 */
public class Parallelism extends BlockJUnit4ClassRunner {

    // Just for Class instance HASH DEMO purposes...not really needed!
    private static final ThreadLocal<Object> test = new ThreadLocal<>();

    public Parallelism(Class<?> clazz) throws Throwable {
        super(clazz);
        setScheduler(new ThreadPoolScheduler());
    }

    @Override
    protected Object createTest() throws Exception {
        test.set(super.createTest());
        return test.get();
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new DumbListener());
        super.run(notifier);
    }

    class DumbListener extends RunListener {

        @Override
        public void testStarted(Description description) throws Exception {
            err.println(format("Class instance: %s Thread ID: %s Thread Name: %s ", test.get().hashCode(), currentThread().getId(), currentThread().getName()));
        }
    }
}
