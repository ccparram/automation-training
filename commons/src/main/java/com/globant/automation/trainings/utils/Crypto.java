package com.globant.automation.trainings.utils;

import com.globant.automation.trainings.logging.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.String.format;

/**
 * @author Juan Krzemien
 */
public class Crypto implements Logging {

    private static final Logger LOG = LoggerFactory.getLogger(Crypto.class);


    public static String md5(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(StandardCharsets.UTF_8.encode(input));
            return format("%032x", new BigInteger(1, md5.digest()));
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
        return "";
    }

}
