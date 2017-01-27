package com.globant.automation.trainings.webdriver.waiting;

import com.globant.automation.trainings.functions.TriFunction;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Juan Krzemien
 */

public class SimpleWaiter<T> {

    private static final int DEFAULT_TIMEOUT_SECS = 30;
    private static final long DEFAULT_POLLING_TIME_MS = 1000;

    private final AtomicInteger timeOut = new AtomicInteger(DEFAULT_TIMEOUT_SECS);
    private final AtomicLong pollingEvery = new AtomicLong(DEFAULT_POLLING_TIME_MS);
    private final AtomicBoolean noFail = new AtomicBoolean(false);

    public SimpleWaiter<T> withTimeOut(int timeOutSeconds) {
        timeOut.set(timeOutSeconds);
        return this;
    }

    public void until(T waitOn, Predicate<T> predicate) {
        try {
            getWaiter(waitOn).until(predicate);
        } catch (TimeoutException toe) {
            if (!noFail.get()) {
                throw toe;
            }
        }
    }

    public <V> V until(T waitOn, Function<? super T, V> condition) {
        try {
            return getWaiter(waitOn).until(condition);
        } catch (TimeoutException toe) {
            if (!noFail.get()) {
                throw toe;
            }
            return null;
        }
    }

    public <K, R> R until(T waitOn, TriFunction<T, K, R> triFunction, K argument) {
        try {
            return getWaiter(waitOn).until((Function<T, R>) input -> triFunction.apply(waitOn, argument));
        } catch (TimeoutException toe) {
            if (!noFail.get()) {
                throw toe;
            }
        }
        return null;
    }

    private FluentWait<T> getWaiter(T waitOn) {
        int timeout = timeOut.get();
        long polling = pollingEvery.get();
        return new FluentWait<>(waitOn)
                .withTimeout(timeout, SECONDS)
                .pollingEvery(polling, MILLISECONDS)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .withMessage(format("Timed out after %s seconds while waiting on %s", timeout, waitOn.toString()));
    }

    public SimpleWaiter<T> withoutFailing() {
        noFail.set(true);
        return this;
    }

    public SimpleWaiter<T> pollingEvery(int everyMs) {
        pollingEvery.set(everyMs);
        return this;
    }

}