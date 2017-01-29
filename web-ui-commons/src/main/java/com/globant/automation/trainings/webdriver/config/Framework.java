package com.globant.automation.trainings.webdriver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.globant.automation.trainings.logging.Logging;
import com.globant.automation.trainings.webdriver.browsers.Browser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Set;

import static java.lang.Thread.currentThread;

/**
 * Global Framework's configuration entry point.
 * <p>
 * Reads a <i>config.yml</i> file expected to be found in classpath (usually <i>src/test/resources</i> for a Maven module)
 *
 * @author Juan Krzemien
 */
public enum Framework implements Logging, Config {

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
        File tmpDir = Environment.isWindows() ? new File("C:/Temp") : new File("/tmp");
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
            configuration = om.readValue(configFile, com.globant.automation.trainings.webdriver.config.impl.ConfigImpl.class);
        } catch (Exception e) {
            getLogger().error("Error parsing framework config!. Re-check!", e);
        }
        return Optional.ofNullable(configuration).orElse(com.globant.automation.trainings.webdriver.config.impl.ConfigImpl.EMPTY);
    }

    @Override
    public boolean isDebugMode() {
        return config.isDebugMode();
    }

    @Override
    public WebDriver WebDriver() {
        return config.WebDriver();
    }

    @Override
    public Driver Driver(Browser browser) {
        return config.Driver(browser);
    }

    @Override
    public Proxy Proxy() {
        return config.Proxy();
    }

    @Override
    public Set<Browser> AvailableDrivers() {
        return config.AvailableDrivers();
    }

}
