package com.globant.automation.trainings.mail;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model from Temp Mail API according to https://temp-mail.ru/en/api/
 *
 * @author Juan Krzemien
 */
public class Mail {

    @JsonProperty("_id")
    private Object id;

    @JsonProperty("createdAt")
    private Object createdAt;

    @JsonProperty("mail_id")
    private String mailId;

    @JsonProperty("mail_address_id")
    private String mailAddressId;

    @JsonProperty("mail_from")
    private String mailFrom;

    @JsonProperty("mail_subject")
    private String mailSubject;

    @JsonProperty("mail_preview")
    private String mailPreview;

    @JsonProperty("mail_text_only")
    private String mailTextOnly;

    @JsonProperty("mail_text")
    private String mailText;

    @JsonProperty("mail_html")
    private String mailHtml;

    @JsonProperty("mail_timestamp")
    private Long mailTimestamp;

    public String getMailId() {
        return mailId;
    }

    public String getMailAddressId() {
        return mailAddressId;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public String getMailPreview() {
        return mailPreview;
    }

    public String getMailTextOnly() {
        return mailTextOnly;
    }

    public String getMailText() {
        return mailText;
    }

    public String getMailHtml() {
        return mailHtml;
    }

    public Long getMailTimestamp() {
        return mailTimestamp;
    }
}
