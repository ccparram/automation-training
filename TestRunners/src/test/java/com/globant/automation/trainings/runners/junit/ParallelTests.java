package com.globant.automation.trainings.runners.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import java.util.LinkedList;

import static java.lang.String.format;
import static java.lang.System.nanoTime;
import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * @author Juan Krzemien
 */

@RunWith(Parallelism.class)
public class ParallelTests {

    private final String browser;
    private final String version;

    private long timeStart;

    public ParallelTests(String browserName, String browserVersion) {
        this.browser = browserName;
        this.version = browserVersion;
    }

    @Parameters(name = "Capability {0} {1} {2}")
    public static LinkedList<String[]> getEnvironments() throws Exception {
        return new LinkedList<String[]>() {
            {
                add(new String[]{"chrome", "50"});
                add(new String[]{"firefox", "latest"});
                add(new String[]{"ie", "9"});
            }
        };
    }

    @Before
    public void setUp() throws Exception {
        out.println("About to run tests in parallel...");
        this.timeStart = nanoTime();
    }

    @Test
    public void testSimple() throws Exception {
        out.println(String.format("Thread %s - Running test using %s / %s", currentThread().getId(), browser, version));
        sleep(1000);
        out.println("Done");
    }

    @After
    public void tearDown() throws Exception {
        out.println(format("Ran tests in parallel in %s ms", MILLISECONDS.convert(nanoTime() - timeStart, NANOSECONDS)));
    }
}