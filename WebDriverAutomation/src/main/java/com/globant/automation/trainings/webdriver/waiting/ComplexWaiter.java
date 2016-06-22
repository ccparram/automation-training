package com.globant.automation.trainings.webdriver.waiting;


import com.globant.automation.trainings.webdriver.waiting.functions.TriFunction;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Juan Krzemien on 6/16/2016.
 */

public class ComplexWaiter<T> {
    private final List<T> thingsToWaitOn = new ArrayList<>();
    private int timeOut = 30;

    public ComplexWaiter() {
    }

    public ComplexWaiter(T waitOn) {
        thingsToWaitOn.add(waitOn);
    }

    public ComplexWaiter<T> withTimeOut(int timeOutSeconds) {
        this.timeOut = timeOutSeconds;
        return this;
    }

    private FluentWait<T> waiterFor(T thing) {
        return new FluentWait<>(thing)
                .pollingEvery(1, SECONDS)
                .withTimeout(timeOut, SECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    public void is(Predicate<T> condition) {
        try {
            thingsToWaitOn.parallelStream().forEach(t -> {
                try {
                    waiterFor(t).until(condition);
                } catch (TimeoutException toe) {
                    String newMessage = format("%s while waiting on %s", toe.getMessage(), t.toString());
                    throw new TimeoutException(newMessage);
                }
            });
        } finally {
            thingsToWaitOn.clear();
        }
    }

    // Just for syntactic sugar...
    public void are(Predicate<T> condition) {
        is(condition);
    }

    public ComplexWaiter<T> on(T thing) {
        return and(thing);
    }

    public ComplexWaiter<T> and(T thing) {
        thingsToWaitOn.add(thing);
        return this;
    }

    public <K, R> R until(final TriFunction<T, K, R> triFunction, final K argument) {
        try {
            R r = thingsToWaitOn.parallelStream().map(t -> waiterFor(t).until(new Function<T, R>() {
                public R apply(T input) {
                    return triFunction.apply(t, argument);
                }
            })).findFirst().orElseThrow(() -> new NoSuchElementException(argument.toString()));
            return r;
        } finally {
            thingsToWaitOn.clear();
        }
    }
}