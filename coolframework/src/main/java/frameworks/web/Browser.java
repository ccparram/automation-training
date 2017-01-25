package frameworks.web;

import frameworks.logging.Logging;
import io.github.bonigarcia.wdm.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

import static frameworks.config.Framework.CONFIGURATION;
import static frameworks.utils.Environment.is64Bits;
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
            initialize(this);
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            capabilities.setCapability("marionette", true);
            return capabilities;
        }
    },
    FIREFOX {
        @Override
        public Capabilities getCapabilities() {
            initialize(this);
            return DesiredCapabilities.firefox();
        }
    },
    CHROME {
        @Override
        public Capabilities getCapabilities() {
            initialize(this);
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
            initialize(this);
            return DesiredCapabilities.internetExplorer();
        }
    },
    EDGE {
        @Override
        public Capabilities getCapabilities() {
            initialize(this);
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

    private static final Architecture architecture = is64Bits() ? x64 : x32;
    private static final Map<Browser, Boolean> alreadyInitialized = new HashMap<>();

    Browser() {
        getLogger().info(format("Initializing [%s] browser capabilities...", name()));
    }

    private synchronized static void initialize(Browser browser) {
        if (alreadyInitialized.computeIfAbsent(browser, b -> false) || CONFIGURATION.WebDriver().isUseSeleniumGrid()) {
            return;
        }
        switch (browser) {
            case MARIONETTE:
            case FIREFOX:
                FirefoxDriverManager.getInstance().setup(architecture, LATEST);
                break;
            case CHROME:
                ChromeDriverManager.getInstance().setup(architecture, LATEST);
                break;
            case IE:
                // Override architecture for IE. 64 bits version is known to misbehave...
                InternetExplorerDriverManager.getInstance().setup(x32, LATEST);
                break;
            case EDGE:
                EdgeDriverManager.getInstance().setup(architecture, LATEST);
                break;
        }
        alreadyInitialized.put(browser, true);
    }
}
