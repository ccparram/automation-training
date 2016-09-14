package com.globant.automation.trainings.servicetesting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.globant.automation.trainings.servicetesting.config.interfaces.IConfig;
import com.globant.automation.trainings.servicetesting.config.interfaces.IProxy;
import com.globant.automation.trainings.servicetesting.logging.Logging;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import static java.lang.Thread.currentThread;

/**
 * Global Framework's configuration entry point.
 * <p>
 * Reads a <i>config.yml</i> file expected to be found in classpath (usually <i>src/test/resources</i> for a Maven module)
 *
 * @author Juan Krzemien
 */
public enum Framework implements IConfig, Logging {

    CONFIGURATION;

    private static final String CONFIG_FILE = "config.yml";

    private final IConfig config;

    Framework() {
        Thread.currentThread().setName("Framework-Thread");
        getLogger().info("Initializing Framework configuration...");
        this.config = readConfig();
    }

    private IConfig readConfig() {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        IConfig configuration = null;
        InputStream configFile = currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
        try {
            configuration = om.readValue(configFile, Config.class);
        } catch (Exception e) {
            getLogger().error("Error parsing framework config!. Re-check!", e);
        }
        return configuration;
    }

    @Override
    public boolean isDebugMode() {
        return config.isDebugMode();
    }


    @Override
    public IProxy Proxy() {
        return config.Proxy();
    }

    @Override
    public Optional<URL> getBaseUrl() {
        return config.getBaseUrl();
    }

}
