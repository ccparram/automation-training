package com.globant.automation.trainings.runners.testng;

import com.globant.automation.trainings.runners.testng.retry.RetryAnalyzer;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.testng.Assert.assertEquals;

/**
 * @author Juan Krzemien
 */
public class Retries {

    @DataProvider(name = "UsernameProvider")
    public Object[][] createData1() {
        return new Object[][]{
                {"Ruso"},
                {"I think I'm gonna fail with this one"},
        };
    }

    @DataProvider(name = "lazilyLoadedData")
    public Iterator<Object[]> createData(ITestContext ctx) { // Argument is optional
        return getDataFromSomewhere()
                .stream()
                .map(s -> new Object[]{s})
                .collect(toList())
                .iterator();
    }

    private List<String> getDataFromSomewhere() {
        // Should be getting the data from a DB, file, etc here instead
        return new ArrayList<>();
    }

    @Test(dataProvider = "UsernameProvider")
    public void parameterizedTest(String username) {
        assertEquals(username, "Ruso");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testToRetry() {
        assertEquals(1, 0);
    }
}