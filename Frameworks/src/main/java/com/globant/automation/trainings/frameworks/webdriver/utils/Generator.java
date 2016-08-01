package com.globant.automation.trainings.frameworks.webdriver.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Juan Krzemien on 7/22/2016.
 */
public class Generator {

    private static final SecureRandom random = new SecureRandom();

    private Generator() {
    }

    public static String randomAlphanumericString(int number) {
        return new BigInteger(8 * number, random).toString(32);
    }
}
