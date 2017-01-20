package com.globant.automation.trainings.runners.testng;

import com.globant.automation.trainings.runners.logging.Logging;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.stream.IntStream;

import static org.testng.Assert.assertEquals;

@Listeners({TestListener.class})
public class Threading implements Logging {

    private Object a;

    @DataProvider(parallel = true)
    public Object[][] data() {
        return IntStream.range(0, 100).mapToObj(name -> new Object[]{}).toArray(Object[][]::new);
    }

    @Test(dataProvider = "data")
    public void test1() {
        Date b = new Date();
        this.a = b;
        assertEquals(a, b, "A is not what I set!");
    }

    @Test(dataProvider = "data")
    public void test2() {
        String b = "Hellow?";
        this.a = b;
        assertEquals(a, b, "A is not what I set!");
    }

    @Test(dataProvider = "data")
    public void test3() {
        Long b = 355345435L;
        this.a = b;
        assertEquals(a, b, "A is not what I set!");
    }

    @Test(dataProvider = "data")
    public void test4() {
        double[] b = new double[4];
        this.a = b;
        assertEquals(a, b, "A is not what I set!");
    }

}
