package frameworks.web;

import frameworks.logging.Logging;
import frameworks.utils.Environment;
import io.github.bonigarcia.wdm.*;
import org.openqa.selenium.Capabilities;
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

public enum Browser implements Logging {

    MARIONETTE {
        @Override
        protected void setupDriver() {
            if (!CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                FirefoxDriverManager.getInstance().setup(architecture, LATEST);
            }
        }

        @Override
        protected Capabilities defineCapabilities() {
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            capabilities.setCapability("marionette", true);
            return capabilities;
        }
    },
    FIREFOX {
        @Override
        protected void setupDriver() {
            if (!CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                FirefoxDriverManager.getInstance().setup(architecture, LATEST);
            }
        }

        @Override
        protected Capabilities defineCapabilities() {
            return DesiredCapabilities.firefox();
        }
    },
    CHROME {
        @Override
        protected void setupDriver() {
            if (!CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                ChromeDriverManager.getInstance().setup(architecture, LATEST);
            }
        }

        @Override
        protected Capabilities defineCapabilities() {
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            options.addArguments(CONFIGURATION.Driver(this).getArguments());
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            return capabilities;
        }
    },
    IE {
        @Override
        protected void setupDriver() {
            if (!CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                // Override architecture for IE. 64 bits version is known to misbehave...
                InternetExplorerDriverManager.getInstance().setup(x32, LATEST);
            }
        }

        @Override
        protected Capabilities defineCapabilities() {
            return DesiredCapabilities.internetExplorer();
        }
    },
    EDGE {
        @Override
        protected void setupDriver() {
            if (!CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
                EdgeDriverManager.getInstance().setup(architecture, LATEST);
            }
        }

        @Override
        protected Capabilities defineCapabilities() {
            return DesiredCapabilities.edge();
        }
    },
    SAFARI {
        @Override
        protected void setupDriver() {
        }

        @Override
        protected Capabilities defineCapabilities() {
            return DesiredCapabilities.safari();
        }
    },
    ANDROID {
        @Override
        protected void setupDriver() {
        }

        @Override
        protected Capabilities defineCapabilities() {
            return DesiredCapabilities.android();
        }
    },
    IPHONE {
        @Override
        protected void setupDriver() {
        }

        @Override
        protected Capabilities defineCapabilities() {
            return DesiredCapabilities.iphone();
        }
    },
    IPAD {
        @Override
        protected void setupDriver() {
        }

        @Override
        protected Capabilities defineCapabilities() {
            return DesiredCapabilities.ipad();
        }
    },
    APPIUM {
        @Override
        protected void setupDriver() {
        }

        @Override
        protected Capabilities defineCapabilities() {
            return new DesiredCapabilities();
        }
    },
    SAUCE_LABS {
        @Override
        protected void setupDriver() {
        }

        @Override
        protected Capabilities defineCapabilities() {
            return new DesiredCapabilities();
        }
    };

    private static Architecture architecture = Environment.is64Bits() ? x64 : x32;

    Browser() {
        getLogger().info(format("Initializing [%s] browser capabilities...", name()));
    }

    public Capabilities getCapabilities() {
        setupDriver();
        return defineCapabilities();
    }

    protected abstract void setupDriver();

    protected abstract Capabilities defineCapabilities();

}
