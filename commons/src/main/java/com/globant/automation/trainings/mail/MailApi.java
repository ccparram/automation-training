package com.globant.automation.trainings.mail;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.globant.automation.trainings.http.RestEasy.createApiFor;
import static com.globant.automation.trainings.logging.Reporter.REPORTER;
import static com.globant.automation.trainings.utils.Crypto.md5;
import static com.globant.automation.trainings.utils.Randomness.nextIntBetween;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

/**
 * Helper class to query a temporary email address
 *
 * @author Juan Krzemien
 */
public class MailApi {

    private MailApi() {
    }

    /**
     * Temp Mail API (https://temp-mail.ru/en/api/)
     *
     * @author Juan Krzemien
     */
    public static class TempMail {

        private static final String MAIL_API_URL = "http://api.temp-mail.ru";
        private static final TempMailApi TEMP_MAIL_API = createApiFor(TempMailApi.class);

        private TempMail() {
        }

        public static String generateValidRandomEmail() {
            List<String> availableMailDomains = getAvailableMailDomains();
            String domain = availableMailDomains.get(nextIntBetween(0, availableMailDomains.size() - 1));
            return "ims_" + randomAlphanumeric(8) + domain;
        }

        private static List<String> getAvailableMailDomains() {
            REPORTER.info("Querying [%s] for available mail domains", MAIL_API_URL);
            return TEMP_MAIL_API.getPossibleMailDomains();
        }

        public static List<Mail> queryMail(String emailAddress) throws NoSuchAlgorithmException {
            REPORTER.info("Querying email for address [%s]", emailAddress);

            List<Mail> response = TEMP_MAIL_API.getMail(md5(emailAddress));

            REPORTER.pass("Mails for [%s] acquired", emailAddress);
            return response;
        }
    }

}
