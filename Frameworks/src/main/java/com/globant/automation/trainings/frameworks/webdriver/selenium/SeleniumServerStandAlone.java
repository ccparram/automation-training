package com.globant.automation.trainings.frameworks.webdriver.selenium;

import com.globant.automation.trainings.frameworks.webdriver.config.Framework;
import org.openqa.selenium.server.SeleniumServer;
import org.slf4j.Logger;

import java.net.BindException;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Launches a single instance of Selenium Server programmatically
 * <p>
 * Created by jkrzemien on 06/07/2016.
 */

public enum SeleniumServerStandAlone {

    INSTANCE;

    private final Logger log = getLogger(SeleniumServerStandAlone.class);
    private SeleniumServer server;

    SeleniumServerStandAlone() {
        if (Framework.CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
            log.info("Using Selenium Grid...");
            return;
        }
        log.info("Launching local Selenium Stand Alone Server...");
        try {
            Map<String, Object> options = new HashMap<String, Object>() {
                {

                }
            };
            this.server = new SeleniumServer(options);
            server.start();
        } catch (BindException be) {
            log.error("Selenium Server seems be to already running on port TCP 4444. Will reuse...");
        } catch (Exception e) {
            log.error("Failed to start the server", e);
        }
    }

    public void shutdown() {
        if (Framework.CONFIGURATION.WebDriver().isUseSeleniumGrid()) return;
        log.info("Shutting down local Selenium Stand Alone Server...");
        server.stop();
        log.info("Done");
    }
}

