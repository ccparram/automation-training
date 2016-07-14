package com.globant.automation.trainings.frameworks.webdriver.enums;

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
            return DesiredCapabilities.chrome();
        }
    },
    IE {
        @Override
        public Capabilities getCapabilities() {
            return DesiredCapabilities.internetExplorer();
        }
    },
    EDGE {
        @Override
        public Capabilities getCapabilities() {
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
    };

    public abstract Capabilities getCapabilities();

}
