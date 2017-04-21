package com.globant.automation.trainings.mail;

import com.globant.automation.trainings.utils.Crypto;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.globant.automation.trainings.logging.Reporter.REPORTER;
import static com.globant.automation.trainings.utils.Marshalling.JSON;
import static com.globant.automation.trainings.utils.Randomness.nextIntBetween;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

/**
 * Helper class to query a temporary email address
 * <p>
 * Temp Mail API (https://temp-mail.ru/en/api/)
 *
 * @author Juan Krzemien
 */
public class TempMail {

    private static final Logger LOG = LoggerFactory.getLogger(TempMail.class);
    private static final String MAIL_API_URL = "http://api.temp-mail.ru";

    static {
        // Only one time
        Unirest.setObjectMapper(new ObjectMapper() {
            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return JSON.from(value, valueType);
                } catch (IOException e) {
                    LOG.error(e.getLocalizedMessage(), e);
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(Object value) {
                try {
                    return JSON.to(value);
                } catch (IOException e) {
                    LOG.error(e.getLocalizedMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private TempMail() {
    }

    public static String generateValidRandomEmail() throws UnirestException {
        List<String> availableMailDomains = getAvailableMailDomains();
        String domain = availableMailDomains.get(nextIntBetween(0, availableMailDomains.size() - 1));
        return randomAlphanumeric(10) + domain;
    }

    private static List<String> getAvailableMailDomains() throws UnirestException {
        REPORTER.info("Querying [%s] for available mail domains", MAIL_API_URL);
        return asList(Unirest
                .get(format("%s/request/domains/format/json/", MAIL_API_URL))
                .asObject(String[].class)
                .getBody());
    }

    public static List<Mail> queryMail(String emailAddress) throws UnirestException, IOException {
        REPORTER.info("Querying email for address [%s]", emailAddress);
        HttpResponse<String> response = Unirest
                .get(format("%s/request/mail/id/%s/format/json/", MAIL_API_URL, Crypto.md5(emailAddress)))
                .asString();

        return response.getStatus() == 200 ? asList(JSON.from(response.getBody(), Mail[].class)) : emptyList();

    }

}
