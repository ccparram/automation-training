package com.globant.automation.trainings.frameworks.webdriver.server;

import com.globant.automation.trainings.frameworks.webdriver.config.Framework;
import org.openqa.selenium.server.SeleniumServer;
import org.slf4j.Logger;

import java.net.BindException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Launches a single instance of Selenium Server programmatically
 * <p>
 * Created by jkrzemien on 06/07/2016.
 */

public enum SeleniumServerStandAlone {

    INSTANCE;

    private final Logger log = getLogger(Framework.class);
    private SeleniumServer server;

    SeleniumServerStandAlone() {
        try {
            this.server = new SeleniumServer();
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

