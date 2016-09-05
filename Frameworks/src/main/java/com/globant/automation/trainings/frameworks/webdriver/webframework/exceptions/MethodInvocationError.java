package com.globant.automation.trainings.frameworks.webdriver.webframework.exceptions;

/**
 * @author Juan Krzemien
 */
public class MethodInvocationError extends Error {

    public MethodInvocationError(String msg) {
        super(msg);
    }

    public MethodInvocationError(String msg, Exception e) {
        super(msg, e);
    }

}
