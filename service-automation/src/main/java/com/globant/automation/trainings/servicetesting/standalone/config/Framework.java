package com.globant.automation.trainings.servicetesting.standalone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.servicetesting.standalone.config.impl.ConfigImpl;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;

/**
 * Global Framework's configuration entry point.
 * <p>
 * Reads a <i>config.yml</i> file expected to be found in classpath (usually <i>src/test/resources</i> for a Maven module)
 *
 * @author Juan Krzemien
 */
public enum Framework implements Config, Logging {

    CONFIGURATION;

    private static final String CONFIG_FILE = "config.yml";

    private final Config config;

    Framework() {
        Thread.currentThread().setName("Framework-Thread");
        getLogger().info("Initializing Framework configuration...");
        this.config = readConfig();
    }

    private Config readConfig() {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        Config configuration = null;
        InputStream configFile = currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
        try {
            configuration = om.readValue(configFile, ConfigImpl.class);
        } catch (Exception e) {
            getLogger().error(format("Error parsing framework config file [%s]. Re-check!", CONFIG_FILE), e);
        }
        return Optional.ofNullable(configuration).orElse(ConfigImpl.EMPTY);
    }

    @Override
    public boolean isDebugMode() {
        return config.isDebugMode();
    }


    @Override
    public Proxy Proxy() {
        return config.Proxy();
    }

    @Override
    public URL getBaseUrl() throws MalformedURLException {
        return config.getBaseUrl();
    }

}
