package com.globant.automation.trainings.runners.testng;

import com.globant.automation.trainings.runners.logging.Logging;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * DEMO test suite to show the crappy behaviour of TestNG's parallel="instances"
 *
 * @author Juan Krzemien
 */
public class ThreadingInstances implements Logging {

    private static AtomicInteger instanceCount = new AtomicInteger(0);

    public ThreadingInstances() {
        instanceCount.incrementAndGet();
        getLogger().info("A new instance of ThreadingInstances created");
    }

    @AfterSuite
    public void printNumberOfInstances() {
        getLogger().info("Number of instances of ThreadingInstances = " + instanceCount.get());
    }

    @Test
    public void testA1() throws Exception {
        getLogger().info("testA1 of instance " + this);
    }

    @Test(dependsOnMethods = "testA1")
    public void testB1() {
        getLogger().info("testB1 of instance " + this);
    }

    @Test(dataProvider = "integerProvider")
    public void testDataProvider(int number) throws Exception {
        getLogger().info("testDataProvider : number = " + number + " test instance = " + this);
    }

    @DataProvider
    public Object[][] integerProvider() {
        return new Object[][]{
                {1},
                {2}
        };
    }

}
