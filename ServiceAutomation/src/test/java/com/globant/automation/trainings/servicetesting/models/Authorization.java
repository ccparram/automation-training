package com.globant.automation.trainings.servicetesting.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Juan Krzemien
 */
public class Authorization {

    @JsonProperty("scopes")
    public List<String> scopes = new ArrayList<>();

    @JsonProperty("note")
    public String note;

    @JsonProperty("note_url")
    public String noteUrl;

    @JsonProperty("client_id")
    public String clientId;

    @JsonProperty("client_secret")
    public String clientSecret;

    @JsonProperty("fingerprint")
    public String fingerprint;
}
