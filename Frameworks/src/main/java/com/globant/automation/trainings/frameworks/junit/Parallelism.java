package com.globant.automation.trainings.frameworks.junit;

import org.junit.runners.Parameterized;
import org.junit.runners.model.RunnerScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * This class is supposed to be used from JUnit's RunWith annotation.
 * Adds support for parallel test runs (default is 5).
 * Extends JUnit's Parameterized class, so you still get the parameterized tests :)
 *
 * @author Juan Krzemien
 */
public class Parallelism extends Parameterized {

    private static class ThreadPoolScheduler implements RunnerScheduler {
        private final ExecutorService executor;

        ThreadPoolScheduler() {
            String threads = System.getProperty("junit.parallel.threads", "5");
            int numThreads = Integer.parseInt(threads);
            executor = Executors.newFixedThreadPool(numThreads);
        }

        @Override
        public void finished() {
            executor.shutdown();
            try {
                executor.awaitTermination(10, MINUTES);
            } catch (InterruptedException exc) {
                throw new RuntimeException(exc);
            }
        }

        @Override
        public void schedule(Runnable childStatement) {
            executor.submit(childStatement);
        }
    }

    public Parallelism(Class<?> clazz) throws Throwable {
        super(clazz);
        setScheduler(new ThreadPoolScheduler());
    }
}