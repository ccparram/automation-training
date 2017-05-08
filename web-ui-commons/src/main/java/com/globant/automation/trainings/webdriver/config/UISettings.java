package com.globant.automation.trainings.webdriver.config;

import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.browsers.Browser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Set;

import static com.globant.automation.trainings.utils.Environment.isWindows;
import static com.globant.automation.trainings.utils.Marshalling.YAML;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;

/**
 * UI specific configuration entry point.
 * <p>
 * Reads a <i>ui-settings.yml</i> file expected to be found in classpath (usually <i>src/test/resources</i> for a Maven module)
 *
 * @author Juan Krzemien
 */
public enum UISettings implements Logging {

    UI;

    private static final String CONFIG_FILE = "ui-settings.yml";

    private final UIConfig config;

    UISettings() {
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
                getLogger().error(e.getMessage(), e);
            }
        }
        System.setProperty("wdm.targetPath", tmpDir.getAbsolutePath());
    }

    private UIConfig readConfig() {
        UIConfig configuration = null;
        InputStream configFile = currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
        try {
            configuration = YAML.from(configFile, UIConfig.class);
        } catch (Exception e) {
            getLogger().error(format("Error parsing framework config file [%s]. Re-check!", CONFIG_FILE), e);
        }
        return Optional.ofNullable(configuration).orElse(UIConfig.EMPTY);
    }

    public WebDriver WebDriver() {
        return config.getWebDriver();
    }

    public Driver Driver(Browser browser) {
        return config.getDriver(browser);
    }

    public Set<Browser> AvailableDrivers() {
        return config.getAvailableDrivers();
    }

}
