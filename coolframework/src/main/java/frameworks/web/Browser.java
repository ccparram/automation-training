package frameworks.web;

import frameworks.logging.Logging;
import frameworks.utils.Environment;
import io.github.bonigarcia.wdm.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import static frameworks.config.Framework.CONFIGURATION;
import static io.github.bonigarcia.wdm.Architecture.x32;
import static io.github.bonigarcia.wdm.Architecture.x64;
import static java.lang.String.format;
import static java.util.jar.Pack200.Packer.LATEST;

/**
 * Enumeration that defines the browsers supported by this framework
 *
 * @author Juan Krzemien
 */

public enum Browser implements Logging, HasCapabilities {

    MARIONETTE {
        @Override
        public Capabilities getCapabilities() {
            if (!CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                FirefoxDriverManager.getInstance().setup(architecture, LATEST);
            }
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            capabilities.setCapability("marionette", true);
            return capabilities;
        }
    },
    FIREFOX {
        @Override
        public Capabilities getCapabilities() {
            if (!CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                FirefoxDriverManager.getInstance().setup(architecture, LATEST);
            }
            return DesiredCapabilities.firefox();
        }
    },
    CHROME {
        @Override
        public Capabilities getCapabilities() {
            if (!CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                ChromeDriverManager.getInstance().setup(architecture, LATEST);
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
                InternetExplorerDriverManager.getInstance().setup(x32, LATEST);
            }
            return DesiredCapabilities.internetExplorer();
        }
    },
    EDGE {
        @Override
        public Capabilities getCapabilities() {
            if (!CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                EdgeDriverManager.getInstance().setup(architecture, LATEST);
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

    private static Architecture architecture = Environment.is64Bits() ? x64 : x32;

    Browser() {
        getLogger().info(format("Initializing [%s] browser capabilities...", name()));
    }

}
