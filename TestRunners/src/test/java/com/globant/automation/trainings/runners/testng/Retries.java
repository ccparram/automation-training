package com.globant.automation.trainings.runners.testng;

import com.globant.automation.trainings.runners.logging.Logging;
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
public class Retries implements Logging {

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
        getLogger().info("Look at me! I'm parameter: " + username);
        assertEquals(username, "Ruso", "While comparing user names:");
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testToRetry() {
        getLogger().info("I say 0 equals 1! It must be equal! This is my example! You can't tell me what to do...");
        assertEquals(1, 0, "I'm gonna force math here:");
    }
}