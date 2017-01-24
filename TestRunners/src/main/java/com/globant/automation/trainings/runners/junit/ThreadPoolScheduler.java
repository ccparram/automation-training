package com.globant.automation.trainings.runners.junit;

import com.globant.automation.trainings.runners.logging.Logging;
import org.junit.runners.model.RunnerScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * This class is supposed to be used from within a JUnit's runner.
 * Adds support for parallel test runs (default is double the number of available processors).
 *
 * @author Juan Krzemien
 */
public class ThreadPoolScheduler implements RunnerScheduler, Logging {

    private static final int TIMEOUT_MINUTES = 10;

    private final ExecutorService executor;

    ThreadPoolScheduler() {
        this.executor = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() * 2), new TestThreadPool());
    }

    @Override
    public void finished() {
        executor.shutdown();
        try {
            executor.awaitTermination(TIMEOUT_MINUTES, MINUTES);
        } catch (InterruptedException exc) {
            throw new RuntimeException(exc);
        }
        getLogger().info(format("Executor state: %s", executor.toString()));
    }

    @Override
    public void schedule(Runnable childStatement) {
        executor.submit(childStatement);
    }

    static class TestThreadPool implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "TestPool-Thread-" + poolNumber.getAndIncrement());
        }
    }
}
