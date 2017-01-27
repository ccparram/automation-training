package com.globant.automation.trainings.webdriver.waiting;


import com.globant.automation.trainings.functions.TriFunction;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.openqa.selenium.TimeoutException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * @author Juan Krzemien
 */
public class ComplexWaiter<T> {

    private final SimpleWaiter<T> simpleWaiter = new SimpleWaiter<>();
    private final List<T> thingsToWaitOn = new ArrayList<>();

    public ComplexWaiter(T waitOn) {
        thingsToWaitOn.add(waitOn);
    }

    public void is(Predicate<T> condition) {
        try {
            final StringBuilder messages = new StringBuilder();
            long processed = thingsToWaitOn.parallelStream().filter(t -> {
                try {
                    simpleWaiter.until(t, condition);
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

    public <V> void is(Function<? super T, V> condition) {
        try {
            final StringBuilder messages = new StringBuilder();
            long processed = thingsToWaitOn.parallelStream()
                    .map(t -> {
                        try {
                            return simpleWaiter.until(t, condition);
                        } catch (TimeoutException toe) {
                            messages.append(format("%s while waiting on %s", toe.getMessage(), t.toString())).append("\n");
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .count();
            if (processed != thingsToWaitOn.size()) {
                throw new TimeoutException(messages.toString());
            }
        } finally {
            thingsToWaitOn.clear();
        }
    }

    public <K, R> List<R> is(final TriFunction<T, K, R> triFunction, final K argument) {
        try {
            return thingsToWaitOn.parallelStream().map(t -> simpleWaiter.until(t, triFunction, argument)).filter(Objects::nonNull).collect(toList());
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