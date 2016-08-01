package com.globant.automation.trainings.frameworks.webdriver.enums;

import com.globant.automation.trainings.frameworks.webdriver.utils.Environment;
import io.github.bonigarcia.wdm.Architecture;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Enumeration that defines the browsers supported by this framework
 *
 * @author Juan Krzemien
 */
public enum Browser {

    FIREFOX {
        @Override
        public Capabilities getCapabilities() {
            return DesiredCapabilities.firefox();
        }
    },
    CHROME {
        @Override
        public Capabilities getCapabilities() {
            ChromeDriverManager.getInstance().setup(architecture);
            return DesiredCapabilities.chrome();
        }
    },
    IE {
        @Override
        public Capabilities getCapabilities() {
            InternetExplorerDriverManager.getInstance().setup(architecture);
            return DesiredCapabilities.internetExplorer();
        }
    },
    EDGE {
        @Override
        public Capabilities getCapabilities() {
            /**
             * FIXME: EdgeDriverManager fails to download the correct Edge driver
             * Microsoft changed the download URL/page so now this Manager crap with its hardcoded HtmlUnit
             * mini-script looks in the wrong place...I don't want the Insiders old version...
             */
            EdgeDriverManager.getInstance().setup();
            return DesiredCapabilities.edge();
        }
    },
    SAFARI {
        @Override
        public Capabilities getCapabilities() {
            return DesiredCapabilities.safari();
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
