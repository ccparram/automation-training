package com.globant.automation.trainings.webdriver.waiting;

import com.globant.automation.trainings.functions.TriFunction;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.FluentWait;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Juan Krzemien
 */

public class SimpleWaiter<T> {

    private final FluentWait<T> wait;
    private final boolean shouldFail;
    private final T waitOn;

    private SimpleWaiter(T waitOn, int timeout, long polling, boolean shouldFail) {
        this.waitOn = waitOn;
        this.wait = new FluentWait<>(waitOn)
                .withTimeout(timeout, SECONDS)
                .pollingEvery(polling, MILLISECONDS)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .withMessage(format("Timed out after %s seconds while waiting on %s", timeout, waitOn.toString()));
        this.shouldFail = shouldFail;
    }

    public void until(Predicate<T> predicate) {
        try {
            wait.until(predicate);
        } catch (TimeoutException toe) {
            if (shouldFail) {
                throw toe;
            }
        }
    }

    public <V> V until(Function<? super T, V> condition) {
        try {
            return wait.until(condition);
        } catch (TimeoutException toe) {
            if (shouldFail) {
                throw toe;
            }
            return null;
        }
    }

    public <K, R> R until(TriFunction<T, K, R> containsInUrl, final K argument) {
        try {
            return wait.until((Function<? super T, R>) k -> containsInUrl.apply(waitOn, argument));
        } catch (TimeoutException toe) {
            if (shouldFail) {
                throw toe;
            }
            return null;
        }
    }

    public static class Waiter<T> {
        private static final int DEFAULT_TIMEOUT_SECS = 30;
        private static final long DEFAULT_POLLING_TIME_MS = 1000;

        private int timeOut = DEFAULT_TIMEOUT_SECS;
        private long pollingEvery = DEFAULT_POLLING_TIME_MS;
        private boolean shouldFail = true;

        public Waiter<T> withTimeOut(int timeOutSeconds) {
            this.timeOut = timeOutSeconds;
            return this;
        }

        public Waiter<T> withoutFailing() {
            this.shouldFail = false;
            return this;
        }

        public Waiter<T> pollingEvery(int everyMs) {
            this.pollingEvery = everyMs;
            return this;
        }

        public SimpleWaiter<T> on(T waitOn) {
            return new SimpleWaiter<>(waitOn, timeOut, pollingEvery, shouldFail);
        }

        public boolean isShouldFail() {
            return shouldFail;
        }
    }
}