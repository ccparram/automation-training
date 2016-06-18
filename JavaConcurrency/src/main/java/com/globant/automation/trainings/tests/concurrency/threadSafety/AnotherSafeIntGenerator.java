package com.globant.automation.trainings.tests.concurrency.threadSafety;

import com.globant.automation.trainings.tests.concurrency.threadSafety.interfaces.IIntGenerator;

/**
 * Created by Juan Krzemien on 6/17/2016.
 */
class AnotherSafeIntGenerator implements IIntGenerator {
    private Integer i = 0;

    synchronized public Integer getNextInt() {
        return ++i;
    }

}
