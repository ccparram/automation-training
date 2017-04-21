package com.globant.automation.trainings.webdriver.server;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.globant.automation.trainings.logging.Logging;
import org.openqa.grid.internal.utils.configuration.StandaloneConfiguration;
import org.openqa.selenium.remote.server.SeleniumServer;
import org.slf4j.LoggerFactory;

import java.net.BindException;
import java.util.logging.LogManager;

import static com.globant.automation.trainings.config.CommonSettings.COMMON;
import static com.globant.automation.trainings.webdriver.config.UISettings.UI;


/**
 * Launches a single instance of Selenium Server programmatically
 *
 * @author Juan Krzemien
 */
public enum SeleniumServerStandAlone implements Logging {

    INSTANCE;

    private SeleniumServer server;

    SeleniumServerStandAlone() {
        if (UI.WebDriver().isSeleniumGrid()) {
            getLogger().info("Using Selenium Grid...");
            return;
        }
        getLogger().info("Launching local Selenium Stand Alone Server...\n");
        try {
            StandaloneConfiguration options = new StandaloneConfiguration();
            options.debug = COMMON.isDebugMode();
            if (!options.debug) {
                // Turn off verbose logging from Selenium Server...(default is ON)
                ((Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.OFF);
                LogManager.getLogManager().getLogger("").setLevel(java.util.logging.Level.OFF);
            }
            this.server = new SeleniumServer(options);
            server.boot();
        } catch (Exception t) {
            if (t.getCause() instanceof BindException) {
                getLogger().error("Already running. Will reuse...");
                return;
            }
            getLogger().error("Failed to start the server", t);
        }
    }

    public void shutdown() {
        if (UI.WebDriver().isSeleniumGrid()) {
            return;
        }
        getLogger().info("Shutting down local Selenium Stand Alone Server...");
        try {
            server.stop();
        } catch (RuntimeException ignored) {
            // Depending on when shutdown() is called, JVM may be already killing the thread
            getLogger().debug(ignored.getLocalizedMessage(), ignored);
        }
        getLogger().info("Done");
    }
}

