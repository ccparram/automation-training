package com.globant.automation.trainings.tests.concurrency.threadSafety;

import com.globant.automation.trainings.tests.concurrency.threadSafety.interfaces.IIntGenerator;

/**
 * Created by Juan Krzemien on 6/17/2016.
 */
class UnsafeIntGenerator implements IIntGenerator {
    private Integer i = 0;

    public Integer getNextInt() {
        return ++i;
    }

}
