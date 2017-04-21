package com.globant.automation.trainings.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.globant.automation.trainings.languages.Language;
import com.globant.automation.trainings.logging.Logging;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

import static com.globant.automation.trainings.utils.Marshalling.YAML;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;

/**
 * General settings/configuration entry point.
 * <p>
 * Reads a <i>settings.yml</i> file expected to be found in classpath (usually <i>src/test/resources</i> for a Maven module)
 *
 * @author Juan Krzemien
 */
public enum CommonSettings implements Logging {

    COMMON;

    private static final String CONFIG_FILE = "settings.yml";

    private final Config config;

    CommonSettings() {
        Thread.currentThread().setName("Framework-Thread");
        getLogger().info("Initializing common settings configuration...");
        this.config = readConfig();
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        if (config.isDebugMode()) {
            root.setLevel(Level.DEBUG);
        } else {
            root.setLevel(Level.INFO);
        }
    }

    private Config readConfig() {
        Config configuration = null;
        InputStream configFile = currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
        try {
            configuration = YAML.from(configFile, Config.class);
        } catch (Exception e) {
            getLogger().error(format("Error parsing framework config file [%s]. Re-check!", CONFIG_FILE), e);
        }
        return Optional.ofNullable(configuration).orElse(Config.EMPTY);
    }

    public boolean isDebugMode() {
        return config.isDebugMode();
    }

    public Proxy proxy() {
        return config.getProxy();
    }

    public Set<Language> availableLanguages() {
        return config.getAvailableLanguages();
    }

    public Environment environment() {
        return config.getActiveEnvironment();
    }

}
