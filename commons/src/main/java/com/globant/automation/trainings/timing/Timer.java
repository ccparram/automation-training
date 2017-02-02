package com.globant.automation.trainings.timing;

import com.globant.automation.trainings.logging.Logging;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

/**
 * Time your code runs!
 * <p>
 * Usage:
 * <p>
 * new Timer("Method doThingsInParallel") {{
 * // code to time
 * }}.timeMe();
 * <p>
 * Notice the {{ and }} between constructor and method call!
 *
 * @author Juan Krzemien
 */
public class Timer implements Logging {

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
