package com.globant.automation.trainings.webframework.exceptions;

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
