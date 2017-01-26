package com.globant.automation.trainings.tests.concurrency.threadSafety;

import com.globant.automation.trainings.tests.concurrency.threadSafety.interfaces.IIntGenerator;

/**
 * @author Juan Krzemien
 */
class UnsafeIntGenerator implements IIntGenerator {
    private Integer i = 0;

    public Integer getNextInt() {
        return ++i;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
