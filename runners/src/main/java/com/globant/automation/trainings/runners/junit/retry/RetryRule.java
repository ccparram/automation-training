package com.globant.automation.trainings.runners.junit.retry;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;
import static java.lang.System.err;

public class RetryRule implements TestRule {

    private AtomicInteger retryCount;

    public RetryRule(int retries) {
        this.retryCount = new AtomicInteger(retries);
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                while (retryCount.getAndDecrement() > 0) {
                    try {
                        base.evaluate();
                        return;
                    } catch (Throwable t) {
                        if (retryCount.get() > 0 && description.getAnnotation(Retry.class) != null) {
                            err.println(format("%s FAILED with %s, %d retries remaining", description.getDisplayName(), t.getClass().getSimpleName(), retryCount.get()));
                        } else {
                            throw t;
                        }
                    }
                }
            }
        };
    }
}