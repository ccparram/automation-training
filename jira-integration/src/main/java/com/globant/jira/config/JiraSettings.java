package com.globant.jira.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.globant.jira.logging.Logging;

import java.io.InputStream;
import java.util.Optional;

import static java.lang.Thread.currentThread;

/**
 * Jira specific configuration entry point.
 * <p>
 * Reads a <i>jira.yml</i> file expected to be found in classpath (usually <i>src/test/resources</i> for a Maven module)
 *
 * @author Juan Krzemien
 */
public enum JiraSettings implements Logging {

    CONFIGURATION;

    private static final String CONFIG_FILE = "jira.yml";

    private final Jira jira;

    JiraSettings() {
        getLogger().info("Initializing Jira configuration...");
        this.jira = readConfig();
    }

    private Jira readConfig() {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        Jira configuration = null;
        InputStream configFile = currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
        try {
            configuration = om.readValue(configFile, Jira.class);
        } catch (Exception e) {
            getLogger().error("Error parsing Jira config!. Re-check!", e);
        }
        return Optional.ofNullable(configuration).orElse(Jira.DEFAULT);
    }

    public Jira Jira() {
        return jira;
    }

}
