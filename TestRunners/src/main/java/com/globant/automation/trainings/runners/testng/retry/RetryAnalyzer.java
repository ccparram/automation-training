package com.globant.automation.trainings.runners.testng.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Juan Krzemien
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final int MAX_RETRIES = 3;
    private AtomicInteger count = new AtomicInteger(MAX_RETRIES);

    @Override
    public boolean retry(ITestResult result) {
        return 0 < count.getAndDecrement();
    }
}
