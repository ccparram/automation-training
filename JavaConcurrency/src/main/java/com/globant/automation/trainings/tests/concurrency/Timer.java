package com.globant.automation.trainings.tests.concurrency;

import com.globant.automation.trainings.tests.concurrency.logging.Logging;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

/**
 * @author Juan Krzemien
 */
public class Timer implements Logging{

    private final String timerName;
    private long started;

    public Timer(String timerName) {
        this.timerName = timerName;
        this.started = currentTimeMillis();
    }

    public void timeMe() {
        getLogger().info(format("Execution of '%s' took %d ms.", timerName, currentTimeMillis() - started));
    }

}
