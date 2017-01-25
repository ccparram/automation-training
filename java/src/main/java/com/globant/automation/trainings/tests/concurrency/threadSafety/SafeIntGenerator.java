package com.globant.automation.trainings.tests.concurrency.threadSafety;

import com.globant.automation.trainings.tests.concurrency.threadSafety.interfaces.IIntGenerator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Juan Krzemien
 */
class SafeIntGenerator implements IIntGenerator {
    private final AtomicInteger i = new AtomicInteger(0);

    public Integer getNextInt() {
        return i.incrementAndGet();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
