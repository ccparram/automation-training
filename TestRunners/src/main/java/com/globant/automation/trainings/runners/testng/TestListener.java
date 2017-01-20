package com.globant.automation.trainings.runners.testng;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import static java.lang.String.format;
import static java.lang.System.err;
import static java.lang.Thread.currentThread;

/**
 * @author Juan Krzemien
 */
public class TestListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        int hash = method.getTestMethod().getRealClass().hashCode();
        err.println(format("Class instance: %s Thread ID: %s Thread Name: %s ", hash, currentThread().getId(), currentThread().getName()));
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {

    }
}
