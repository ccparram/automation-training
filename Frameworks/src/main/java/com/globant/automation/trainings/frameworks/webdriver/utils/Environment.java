package com.globant.automation.trainings.frameworks.webdriver.utils;

import static java.lang.System.getProperty;

public class Environment {

    private static final String OS = getProperty("os.name").toLowerCase();
    private static final String ARCH = getProperty("os.arch", "");

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isUnix() {
        return OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
    }

    public static boolean is64Bits() {
        return ARCH.contains("64");
    }
}
