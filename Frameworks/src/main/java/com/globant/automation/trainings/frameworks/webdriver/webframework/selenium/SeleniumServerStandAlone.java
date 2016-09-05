package com.globant.automation.trainings.frameworks.webdriver.webframework.selenium;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.openqa.grid.internal.utils.configuration.StandaloneConfiguration;
import org.openqa.selenium.remote.server.SeleniumServer;
import org.slf4j.LoggerFactory;

import java.net.BindException;
import java.util.logging.LogManager;

import static com.globant.automation.trainings.frameworks.webdriver.webframework.config.Framework.CONFIGURATION;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Launches a single instance of Selenium Server programmatically
 *
 * @author Juan Krzemien
 */

public enum SeleniumServerStandAlone {

    INSTANCE;

    private final org.slf4j.Logger log = getLogger(SeleniumServerStandAlone.class);
    private SeleniumServer server;

    SeleniumServerStandAlone() {
        if (CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
            log.info("Using Selenium Grid...");
            return;
        }
        log.info("Launching local Selenium Stand Alone Server...");
        try {
            StandaloneConfiguration options = new StandaloneConfiguration();
            options.debug = CONFIGURATION.isDebugMode();
            if (!options.debug) {
                ((Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.OFF);
                LogManager.getLogManager().getLogger("").setLevel(java.util.logging.Level.OFF);
            }
            this.server = new SeleniumServer(options);
            server.boot();
        } catch (Throwable t) {
            if (t.getCause() instanceof BindException) {
                log.error("Already running. Will reuse...");
                return;
            }
            log.error("Failed to start the server", t);
        }
    }

    public void shutdown() {
        if (CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
            return;
        }
        log.info("Shutting down local Selenium Stand Alone Server...");
        server.stop();
        log.info("Done");
    }
}

