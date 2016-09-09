package com.globant.automation.trainings.frameworks.junit.tests;

import com.globant.automation.trainings.frameworks.junit.Parallelism;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.LinkedList;

import static java.lang.String.format;
import static java.lang.System.nanoTime;
import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Created by Juan Krzemien on 6/27/2016.
 */

@RunWith(Parallelism.class)
public class ParallelTests {

    private Capabilities capabilities;
    private long timeStart;

    public ParallelTests(String platform, String browserName, String browserVersion) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platform", platform);
        capabilities.setCapability("browser", browserName);
        capabilities.setCapability("browserVersion", browserVersion);
        capabilities.setCapability("name", "Parallel test");
        this.capabilities = capabilities;
    }

    @Parameterized.Parameters(name = "Capability {0} {1} {2}")
    public static LinkedList<String[]> getEnvironments() throws Exception {
        LinkedList<String[]> env = new LinkedList<>();
        env.add(new String[]{Platform.WINDOWS.toString(), "chrome", "50"});
        env.add(new String[]{Platform.WINDOWS.toString(), "firefox", "latest"});
        env.add(new String[]{Platform.WINDOWS.toString(), "ie", "9"});
        env.add(new String[]{Platform.WINDOWS.toString(), "chrome", "50"});
        env.add(new String[]{Platform.WINDOWS.toString(), "firefox", "latest"});
        env.add(new String[]{Platform.WINDOWS.toString(), "ie", "9"});
        env.add(new String[]{Platform.WINDOWS.toString(), "chrome", "50"});
        env.add(new String[]{Platform.WINDOWS.toString(), "firefox", "latest"});
        env.add(new String[]{Platform.WINDOWS.toString(), "ie", "9"});
        env.add(new String[]{Platform.WINDOWS.toString(), "chrome", "50"});
        env.add(new String[]{Platform.WINDOWS.toString(), "firefox", "latest"});
        env.add(new String[]{Platform.WINDOWS.toString(), "ie", "9"});
        env.add(new String[]{Platform.WINDOWS.toString(), "chrome", "50"});
        env.add(new String[]{Platform.WINDOWS.toString(), "firefox", "latest"});
        env.add(new String[]{Platform.WINDOWS.toString(), "ie", "9"});
        env.add(new String[]{Platform.WINDOWS.toString(), "chrome", "50"});
        env.add(new String[]{Platform.WINDOWS.toString(), "firefox", "latest"});
        env.add(new String[]{Platform.WINDOWS.toString(), "ie", "9"});
        env.add(new String[]{Platform.WINDOWS.toString(), "chrome", "50"});
        env.add(new String[]{Platform.WINDOWS.toString(), "firefox", "latest"});
        env.add(new String[]{Platform.WINDOWS.toString(), "ie", "9"});

        //add more browsers here

        return env;
    }

    @Before
    public void setUp() throws Exception {
        out.println("About to run tests in parallel...");
        this.timeStart = nanoTime();
    }

    @Test
    public void testSimple() throws Exception {
        out.println(format("Thread %s - Running test using %s", currentThread().getId(), capabilities));
        sleep(1000);
        out.println("Done");
    }

    @After
    public void tearDown() throws Exception {
        out.println(format("Ran tests in parallel in %s ms", MILLISECONDS.convert(nanoTime() - timeStart, NANOSECONDS)));
    }
}