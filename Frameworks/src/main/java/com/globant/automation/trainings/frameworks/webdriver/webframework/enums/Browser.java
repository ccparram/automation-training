package com.globant.automation.trainings.frameworks.webdriver.webframework.enums;

import io.github.bonigarcia.wdm.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.globant.automation.trainings.frameworks.webdriver.webframework.config.Framework.CONFIGURATION;
import static com.globant.automation.trainings.frameworks.webdriver.webframework.utils.Environment.is64Bits;
import static io.github.bonigarcia.wdm.Architecture.x32;
import static io.github.bonigarcia.wdm.Architecture.x64;
import static io.github.bonigarcia.wdm.DriverVersion.LATEST;

/**
 * Enumeration that defines the browsers supported by this framework
 *
 * @author Juan Krzemien
 */
public enum Browser {

    MARIONETTE {
        @Override
        public Capabilities getCapabilities() {
            if (!CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                MarionetteDriverManager.getInstance().setup(architecture, LATEST.toString());
            }
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            capabilities.setCapability("marionette", true);
            return capabilities;
        }
    },
    FIREFOX {
        @Override
        public Capabilities getCapabilities() {
            return DesiredCapabilities.firefox();
        }
    },
    CHROME {
        @Override
        public Capabilities getCapabilities() {
            if (!CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                ChromeDriverManager.getInstance().setup(architecture, LATEST.toString());
            }
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();

            ChromeOptions options = new ChromeOptions();
            options.addArguments(CONFIGURATION.Driver(this).getArguments());
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);

            return capabilities;
        }
    },
    IE {
        @Override
        public Capabilities getCapabilities() {
            if (!CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                // Override architecture for IE. 64 bits version is known to misbehave...
                InternetExplorerDriverManager.getInstance().setup(x32, LATEST.toString());
            }
            return DesiredCapabilities.internetExplorer();
        }
    },
    EDGE {
        @Override
        public Capabilities getCapabilities() {
            if (!CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                EdgeDriverManager.getInstance().setup(architecture, LATEST.toString());
            }
            return DesiredCapabilities.edge();
        }
    },
    SAFARI {
        @Override
        public Capabilities getCapabilities() {
            return DesiredCapabilities.safari();
        }
    },
    ANDROID {
        @Override
        public Capabilities getCapabilities() {
            return DesiredCapabilities.android();
        }
    },
    IPHONE {
        @Override
        public Capabilities getCapabilities() {
            return DesiredCapabilities.iphone();
        }
    },
    IPAD {
        @Override
        public Capabilities getCapabilities() {
            return DesiredCapabilities.ipad();
        }
    },
    APPIUM {
        @Override
        public Capabilities getCapabilities() {
            return new DesiredCapabilities();
        }
    },
    SAUCE_LABS {
        @Override
        public Capabilities getCapabilities() {
            return new DesiredCapabilities();
        }
    };

    private static Architecture architecture = is64Bits() ? x64 : x32;

    public abstract Capabilities getCapabilities();

}
