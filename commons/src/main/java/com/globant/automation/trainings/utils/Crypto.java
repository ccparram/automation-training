package com.globant.automation.trainings.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Helper class for cryptographic functions / hashing / (en/de)coding
 *
 * @author Juan Krzemien
 */
public class Crypto {

    private Crypto() {
    }

    public static String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(StandardCharsets.UTF_8.encode(input));
        return String.format("%032x", new BigInteger(1, md5.digest()));
    }

}
