package com.globant.automation.trainings.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Juan Krzemien
 */
public enum Marshalling {

    JSON(null), YAML(new YAMLFactory());

    private final ObjectMapper om;

    Marshalling(JsonFactory factory) {
        this.om = new ObjectMapper(factory);
        om.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    public String to(Object o) throws JsonProcessingException {
        return om.writeValueAsString(o);
    }

    public <K> K from(String json, Class<K> as) throws IOException {
        return om.readValue(json, as);
    }

    public <K> K from(InputStream inputStream, Class<K> as) throws IOException {
        return om.readValue(inputStream, as);
    }
}
