package com.globant.automation.trainings.webdriver.waiting;


import com.globant.automation.trainings.functions.TriFunction;
import com.globant.automation.trainings.logging.Logging;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.openqa.selenium.TimeoutException;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * @author Juan Krzemien
 */
public class ComplexWaiter<T> implements Logging {

    private static final String WHILE_WAITING_ON = "%s while waiting on %s";
    private final SimpleWaiter.Waiter<T> simpleWaiter = new SimpleWaiter.Waiter<>();
    private final List<T> thingsToWaitOn = new CopyOnWriteArrayList<>();

    public ComplexWaiter(T waitOn) {
        thingsToWaitOn.add(waitOn);
    }

    public boolean is(Predicate<T> condition) {
        try {
            simpleWaiter.on(thingsToWaitOn.get(0)).until(condition);
            return true;
        } catch (TimeoutException toe) {
            if (simpleWaiter.isShouldFail()) {
                throw toe;
            }
            return false;
        } finally {
            thingsToWaitOn.clear();
        }
    }

    public <V> V is(Function<? super T, V> condition) {
        try {
            return simpleWaiter.on(thingsToWaitOn.get(0)).until(condition);
        } catch (TimeoutException toe) {
            if (simpleWaiter.isShouldFail()) {
                throw toe;
            }
            return null;
        } finally {
            thingsToWaitOn.clear();
        }
    }

    public <V> List<V> are(Function<? super T, V> condition) {
        try {
            final StringBuilder messages = new StringBuilder();
            List<V> results = thingsToWaitOn.parallelStream()
                    .map(t -> {
                        try {
                            return simpleWaiter.on(t).until(condition);
                        } catch (TimeoutException toe) {
                            getLogger().debug(toe.getLocalizedMessage(), toe);
                            messages.append(format(WHILE_WAITING_ON, toe.getMessage(), t.toString())).append("\n");
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(toList());
            if (results.size() != thingsToWaitOn.size() && simpleWaiter.isShouldFail()) {
                throw new TimeoutException(messages.toString());
            }
            return results;
        } finally {
            thingsToWaitOn.clear();
        }

    }

    public void are(Predicate<T> condition) {
        try {
            final StringBuilder messages = new StringBuilder();
            long processed = thingsToWaitOn.parallelStream()
                    .filter(t -> {
                        try {
                            simpleWaiter.on(t).until(condition);
                            return true;
                        } catch (TimeoutException toe) {
                            getLogger().debug(toe.getLocalizedMessage(), toe);
                            messages.append(format(WHILE_WAITING_ON, toe.getMessage(), t.toString())).append("\n");
                            return false;
                        }
                    })
                    .count();
            if (processed != thingsToWaitOn.size() && simpleWaiter.isShouldFail()) {
                throw new TimeoutException(messages.toString());
            }
        } finally {
            thingsToWaitOn.clear();
        }

    }

    public <K, R> List<R> until(TriFunction<T, K, R> function, K argument) {
        try {
            final StringBuilder messages = new StringBuilder();
            List<R> results = thingsToWaitOn.parallelStream()
                    .map(t -> {
                        try {
                            return simpleWaiter.on(t).until((Function<? super T, R>) k -> function.apply(t, argument));
                        } catch (TimeoutException toe) {
                            getLogger().debug(toe.getLocalizedMessage(), toe);
                            messages.append(format(WHILE_WAITING_ON, toe.getMessage(), t.toString())).append("\n");
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(toList());
            if (results.size() != thingsToWaitOn.size() && simpleWaiter.isShouldFail()) {
                throw new TimeoutException(messages.toString());
            }
            return results;
        } finally {
            thingsToWaitOn.clear();
        }
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