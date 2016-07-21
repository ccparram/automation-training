package com.globant.automation.trainings.frameworks.testng.tests;

import com.globant.automation.trainings.frameworks.testng.retry.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Juan Krzemien on 7/21/2016.
 */
public class Retries {

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testToRetry() {
        Assert.assertEquals(1, 0);
    }

}
