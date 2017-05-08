package com.globant.automation.trainings.timing;

import com.globant.automation.trainings.logging.Logging;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.currentThread;

/**
 * @author Juan Krzemien
 */
public class Timer2 implements AutoCloseable, Logging {

    private final String timerName;
    private final long start = currentTimeMillis();

    public Timer2() {
        this("Unnamed timer @ Thread " + currentThread().getId());
    }

    public Timer2(String name) {
        this.timerName = name;
    }

    @Override
    public void close() {
        getLogger().info(format("Execution of '%s' took %d ms.", timerName, currentTimeMillis() - start));
    }
}