package com.globant.automation.trainings.webdriver.waiting;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.FluentWait;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Juan Krzemien on 6/16/2016.
 */

public class SimpleWaiter {

    private int timeOut = 30;

    public SimpleWaiter withTimeOut(int timeOutSeconds) {
        this.timeOut = timeOutSeconds;
        return this;
    }

    public <T> T waitUntil(T waitOn, Predicate<T> predicate) {
        try {
            new FluentWait<>(waitOn)
                    .withTimeout(timeOut, SECONDS)
                    .pollingEvery(1, SECONDS)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(NoSuchElementException.class)
                    .until(predicate);
        } catch (TimeoutException toe) {
            String newMessage = format("%s while waiting on %s", toe.getMessage(), waitOn.toString());
            throw new TimeoutException(newMessage);
        }
        return waitOn;
    }

    public <F, T> T waitUntil(F waitOn, Function<F, T> function) {
        try {
            return new FluentWait<>(waitOn)
                    .withTimeout(timeOut, SECONDS)
                    .pollingEvery(1, SECONDS)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(NoSuchElementException.class)
                    .until(function);
        } catch (TimeoutException toe) {
            String newMessage = format("%s while waiting on %s", toe.getMessage(), waitOn.toString());
            throw new TimeoutException(newMessage);
        }
    }
}