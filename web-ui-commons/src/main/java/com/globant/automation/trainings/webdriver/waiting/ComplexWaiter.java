package com.globant.automation.trainings.webdriver.waiting;


import com.globant.automation.trainings.functions.TriFunction;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;

/**
 * @author Juan Krzemien
 */
public class ComplexWaiter<T> extends FluentWait<T> {

    private static final int DEFAULT_TIMEOUT_SECS = 30;
    private static final int DEFAULT_POLLING_TIME_MS = 1000;

    private final SimpleWaiter<T> simpleWaiter = new SimpleWaiter<>();
    private final List<T> thingsToWaitOn = new ArrayList<>();

    public ComplexWaiter(T waitOn) {
        super(waitOn);
        withTimeout(DEFAULT_TIMEOUT_SECS, SECONDS);
        pollingEvery(DEFAULT_POLLING_TIME_MS, MILLISECONDS);
        ignoring(StaleElementReferenceException.class);
        ignoring(NoSuchElementException.class);
        withMessage(format("Timed out while waiting on %s", waitOn.toString()));
        thingsToWaitOn.add(waitOn);
    }

    public void is(Predicate<T> condition) {
        resolveIs(condition, null);
    }

    public <K> void is(Function<T, K> condition) {
        resolveIs(null, condition);
    }

    private <K> void resolveIs(final Predicate<T> conditionA, final Function<T, K> conditionB) {
        try {
            final StringBuilder messages = new StringBuilder();
            long processed = thingsToWaitOn.parallelStream().filter(t -> {
                try {
                    if (conditionA == null) {
                        simpleWaiter.until(t, conditionB);
                    } else {
                        simpleWaiter.until(t, conditionA);
                    }
                    return true;
                } catch (TimeoutException toe) {
                    messages.append(format("%s while waiting on %s", toe.getMessage(), t.toString())).append("\n");
                    return false;
                }
            }).count();
            if (processed != thingsToWaitOn.size()) {
                throw new TimeoutException(messages.toString());
            }
        } finally {
            thingsToWaitOn.clear();
        }
    }

    // Just for syntactic sugar...
    public void are(Predicate<T> condition) {
        is(condition);
    }

    public <K> void are(Function<T, K> condition) {
        is(condition);
    }

    public ComplexWaiter<T> and(T thing) {
        thingsToWaitOn.add(thing);
        return this;
    }

    public <K, R> List<R> until(final TriFunction<T, K, R> triFunction, final K argument) {
        try {
            return thingsToWaitOn.parallelStream().map(t -> simpleWaiter.until(t, triFunction, argument)).filter(Objects::nonNull).collect(toList());
        } finally {
            thingsToWaitOn.clear();
        }
    }

    @Override
    public <V> V until(Function<? super T, V> isTrue) {
        try {
            return thingsToWaitOn.parallelStream().map(t -> simpleWaiter.until(t, isTrue)).findFirst().orElse(null);
        } finally {
            thingsToWaitOn.clear();
        }
    }

    @Override
    public void until(Predicate<T> isTrue) {
        try {
            thingsToWaitOn.parallelStream().forEach(t -> simpleWaiter.until(t, isTrue));
        } finally {
            thingsToWaitOn.clear();
        }
    }

    public ComplexWaiter<T> withTimeOut(final int timeOutSeconds) {
        simpleWaiter.withTimeOut(timeOutSeconds);
        return this;
    }

    public ComplexWaiter<T> pollingEvery(int everyMs) {
        simpleWaiter.pollingEvery(everyMs);
        return this;
    }

    public ComplexWaiter<T> withoutFailing() {
        simpleWaiter.withoutFailing();
        return this;
    }
}