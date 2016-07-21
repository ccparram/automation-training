package com.globant.automation.trainings.frameworks.testng.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Juan Krzemien on 7/21/2016.
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private AtomicInteger count = new AtomicInteger(3);

    @Override
    public boolean retry(ITestResult result) {
        return 0 < count.getAndDecrement();
    }
}
