package com.globant.automation.trainings.tests.concurrency.threadSafety;

import com.globant.automation.trainings.tests.concurrency.threadSafety.interfaces.IIntGenerator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Juan Krzemien on 6/17/2016.
 */
class SafeIntGenerator implements IIntGenerator {
    private final AtomicInteger i = new AtomicInteger(0);

    public Integer getNextInt() {
        return i.incrementAndGet();
    }
}
