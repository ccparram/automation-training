package com.globant.automation.trainings.webdriver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.browsers.Browser;
import com.globant.automation.trainings.webdriver.languages.Language;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Set;

import static com.globant.automation.trainings.utils.Environment.isWindows;
import static java.lang.Thread.currentThread;

/**
 * Global Framework's configuration entry point.
 * <p>
 * Reads a <i>config.yml</i> file expected to be found in classpath (usually <i>src/test/resources</i> for a Maven module)
 *
 * @author Juan Krzemien
 */
public enum Framework implements Logging {

    CONFIGURATION;

    private static final String CONFIG_FILE = "config.yml";

    private final Config config;

    Framework() {
        Thread.currentThread().setName("Framework-Thread");
        getLogger().info("Initializing Framework configuration...");
        setDriversDownloadDirectory();
        this.config = readConfig();
    }

    private void setDriversDownloadDirectory() {
        // Define WebDriver's driver download directory once!
        File tmpDir = isWindows() ? new File("C:/Temp") : new File("/tmp");
        if (!tmpDir.exists()) {
            try {
                Files.createDirectory(tmpDir.toPath());
            } catch (IOException e) {
                getLogger().error(e.getMessage());
            }
        }
        System.setProperty("wdm.targetPath", tmpDir.getAbsolutePath());
    }

    private Config readConfig() {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        Config configuration = null;
        InputStream configFile = currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
        try {
            configuration = om.readValue(configFile, Config.class);
        } catch (Exception e) {
            getLogger().error("Error parsing framework config!. Re-check!", e);
        }
        return Optional.ofNullable(configuration).orElse(Config.EMPTY);
    }

    public boolean isDebugMode() {
        return config.isDebugMode();
    }

    public WebDriver WebDriver() {
        return config.getWebDriver();
    }

    public Driver Driver(Browser browser) {
        return config.getDriver(browser);
    }

    public Proxy Proxy() {
        return config.getProxy();
    }

    public Set<Browser> AvailableDrivers() {
        return config.getAvailableDrivers();
    }

    public Set<Language> AvailableLanguages() {
        return config.getAvailableLanguages();
    }

    public Environment Environment() {
        return config.getActiveEnvironment();
    }

}
