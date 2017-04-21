package com.globant.automation.trainings.utils;

import static java.lang.Integer.parseInt;
import static java.lang.Runtime.getRuntime;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;

/**
 * Helper utility class that provides information on JVM's running environment
 *
 * @author Juan Krzemien
 */
public final class Environment {

    private static final String OS = getProperty("os.name").toLowerCase();
    private static final String ARCH = getProperty("os.arch", "");

    private Environment() {
    }

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

    public static int getNumberOfThreads() {
        return parseInt(ofNullable(getProperty("junit.parallel.threads")).orElse(String.valueOf(getRuntime().availableProcessors())));
    }
}
