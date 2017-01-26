package com.globant.automation.trainings.tests.concurrency.threadSafety;

import com.globant.automation.trainings.tests.concurrency.threadSafety.interfaces.IIntGenerator;

/**
 * @author Juan Krzemien
 */
class AnotherSafeIntGenerator implements IIntGenerator {
    private Integer i = 0;

    public synchronized Integer getNextInt() {
        return ++i;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
