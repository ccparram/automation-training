package com.globant.automation.trainings.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.globant.automation.trainings.logging.Logging;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class Environment implements Logging {

    public static final Environment DEFAULT = new Environment();

    @JsonProperty("name")
    private String name = "QA";

    @JsonProperty("baseUrl")
    private String baseUrl = "http://default.base.url";

    @JsonProperty("ports")
    private Map<String, Integer> ports = new HashMap<>();

    private Environment() {
    }

    public String getName() {
        return name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public <T> String getBaseUrl(Class<T> api) throws MalformedURLException {
        // Java proxies append use a final $ in its class package reference
        String apiName = api.getName().replace("$", ".");
        Integer port = ports.get(apiName);
        URL url = new URL(getBaseUrlFromEnvironment().orElse(getBaseUrl()));
        if (port != null) {
            getLogger().info(format("Using port %s to reach API %s", port, apiName));
            url = new URL(url.getProtocol(), url.getHost(), port, url.getFile());
        } else {
            getLogger().warn(format("Port not defined for API %s! You may be missing ports entries in your settings.yml file", apiName));
        }
        return url.toString();
    }

    /**
     * Optionally retrieves SUT_URL environment variable value
     *
     * @return Optional using a possible String value representing the base URL
     */
    private Optional<String> getBaseUrlFromEnvironment() {
        Optional<String> sutUrl = ofNullable(System.getenv("SUT_URL"));
        sutUrl.ifPresent(url -> getLogger().warn(format("Environment variable SUT_URL present!: %s", url)));
        return sutUrl;
    }
}
