package com.globant.automation.trainings.frameworks.webdriver.factories;

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
        try {
            Map<String, Object> options = new HashMap<String, Object>() {
                {

                }
            };
            this.server = new SeleniumServer(options);
            server.start();
        } catch (BindException be) {
            log.error("Already running. Will reuse...");
        } catch (Exception e) {
            log.error("Failed to start the server", e);
        }
    }

    public void shutdown() {
        log.info("Shutting down Selenium Server...");
        server.stop();
        log.info("Done");
    }
}

