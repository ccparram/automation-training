package com.globant.automation.trainings.frameworks.webdriver.enums;

import com.globant.automation.trainings.frameworks.webdriver.config.Framework;
import com.globant.automation.trainings.frameworks.webdriver.utils.Environment;
import io.github.bonigarcia.wdm.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Enumeration that defines the browsers supported by this framework
 *
 * @author Juan Krzemien
 */
public enum Browser {

    MARIONETTE {
        @Override
        public Capabilities getCapabilities() {
            if (!Framework.CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                MarionetteDriverManager.getInstance().setup(architecture, DriverVersion.LATEST.toString());
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
            if (!Framework.CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                ChromeDriverManager.getInstance().setup(architecture, DriverVersion.LATEST.toString());
            }
            ChromeOptions options = new ChromeOptions();
            options.addArguments(Framework.CONFIGURATION.Driver(this).getArguments());
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            return capabilities;
        }
    },
    IE {
        @Override
        public Capabilities getCapabilities() {
            if (!Framework.CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                // Override architecture for IE. 64 bits version is known to misbehave...
                InternetExplorerDriverManager.getInstance().setup(Architecture.x32, DriverVersion.LATEST.toString());
            }
            return DesiredCapabilities.internetExplorer();
        }
    },
    EDGE {
        @Override
        public Capabilities getCapabilities() {
            if (!Framework.CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                /**
                 * FIXME: EdgeDriverManager fails to download the correct Edge driver
                 * Microsoft changed the download URL/page so now this Manager crap with its hardcoded HtmlUnit
                 * mini-script looks in the wrong place...I don't want the Insiders old version...
                 */
                EdgeDriverManager.getInstance().setup();
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
            return null;
        }
    },
    SAUCE_LABS {
        @Override
        public Capabilities getCapabilities() {
            return null;
        }
    };

    private static Architecture architecture = Environment.is64Bits() ? Architecture.x64 : Architecture.x32;

    public abstract Capabilities getCapabilities();

}
