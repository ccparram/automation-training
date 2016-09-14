package com.globant.automation.trainings.webdriver.waiting;

import com.globant.automation.trainings.webdriver.waiting.functions.TriFunction;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.FluentWait;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Juan Krzemien
 */

public class SimpleWaiter {

    private static final int DEFAULT_TIMEOUT = 30;

    private int timeOut = DEFAULT_TIMEOUT;

    public SimpleWaiter withTimeOut(final int timeOutSeconds) {
        this.timeOut = timeOutSeconds;
        return this;
    }

    public <T> T waitUntil(final T waitOn, final Predicate<T> predicate) {
        getWaiter(waitOn).until(predicate);
        return waitOn;
    }

    public <F, T> T waitUntil(final F waitOn, final Function<F, T> function) {
        return getWaiter(waitOn).until(function);
    }

    public <T, K, R> R waitUntil(final T waitOn, final TriFunction<T, K, R> triFunction, final K argument) {
        return getWaiter(waitOn).until(new Function<T, R>() {
            public R apply(T input) {
                return triFunction.apply(waitOn, argument);
            }
        });
    }

    private <T> FluentWait<T> getWaiter(final T waitOn) {
        return new FluentWait<>(waitOn)
                .withTimeout(timeOut, SECONDS)
                .pollingEvery(1, SECONDS)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .withMessage(format("Timed out after %s seconds while waiting on %s", timeOut, waitOn.toString()));
    }

}