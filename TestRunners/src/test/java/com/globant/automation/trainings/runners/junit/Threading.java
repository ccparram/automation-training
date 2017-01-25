package com.globant.automation.trainings.runners.junit;

import com.globant.automation.trainings.runners.logging.Logging;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static java.lang.String.format;
import static java.lang.System.err;
import static java.lang.Thread.currentThread;
import static org.testng.Assert.assertEquals;

/**
 * DEMO test suite to show how JUnit focuses on test isolation. It creates a new class instance for every test method.
 * Thread safety is not a major issue here, although you always should keep an eye on class's state.
 * <p>
 * Run this suite *several* times. It should never fail
 *
 * @author Juan Krzemien
 */
@RunWith(Parallelism.class)
public class Threading implements Logging {

    private boolean a;

    public Threading() {
        err.println(format("IN CONSTRUCTOR: Class instance: %s Thread ID: %s Thread Name: %s ", this.hashCode(), currentThread().getId(), currentThread().getName()));
    }

    @Test
    public void test1() {
        boolean b = new Random().nextBoolean();
        a = b;
        assertEquals(a, b, "A is not what I set!");
    }

    @Test
    public void test2() {
        boolean b = new Random().nextBoolean();
        a = b;
        assertEquals(a, b, "A is not what I set!");
    }

    @Test
    public void test3() {
        boolean b = new Random().nextBoolean();
        a = b;
        assertEquals(a, b, "A is not what I set!");
    }

    @Test
    public void test4() {
        boolean b = new Random().nextBoolean();
        a = b;
        assertEquals(a, b, "A is not what I set!");
    }

}
