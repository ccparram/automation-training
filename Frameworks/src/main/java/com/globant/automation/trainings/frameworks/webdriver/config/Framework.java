package com.globant.automation.trainings.frameworks.webdriver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.globant.automation.trainings.frameworks.webdriver.config.interfaces.IConfig;
import com.globant.automation.trainings.frameworks.webdriver.config.interfaces.IDriver;
import com.globant.automation.trainings.frameworks.webdriver.config.interfaces.IProxy;
import com.globant.automation.trainings.frameworks.webdriver.config.interfaces.IWebDriverConfig;
import com.globant.automation.trainings.frameworks.webdriver.enums.Browser;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.Set;

import static com.globant.automation.trainings.frameworks.webdriver.enums.Browser.CHROME;
import static com.globant.automation.trainings.frameworks.webdriver.enums.Browser.IE;
import static java.lang.Thread.currentThread;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Juan Krzemien
 */
public enum Framework implements IConfig {

    CONFIGURATION;

    private final Logger log = getLogger(Framework.class);
    private final IConfig config;
    private static final String CONFIG_FILE = "config.yml";

    Framework() {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        IConfig configuration = null;
        InputStream configFile = currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
        try {
            configuration = om.readValue(configFile, Config.class);
        } catch (Exception e) {
            log.error("Error parsing framework config!. Re-check!", e);
        }
        this.config = configuration;
        if (config != null) {
            if (config.AvailableDrivers().contains(CHROME)) {
                System.setProperty("webdriver.chrome.driver", config.Driver(CHROME).getPath());
            }
            if (config.AvailableDrivers().contains(IE)) {
                System.setProperty("webdriver.ie.driver", config.Driver(IE).getPath());
            }
        }
    }

    @Override
    public IWebDriverConfig WebDriver() {
        return config.WebDriver();
    }

    @Override
    public IDriver Driver(Browser browser) {
        return config.Driver(browser);
    }

    @Override
    public IProxy Proxy() {
        return config.Proxy();
    }

    @Override
    public Set<Browser> AvailableDrivers() {
        return config.AvailableDrivers();
    }

}
