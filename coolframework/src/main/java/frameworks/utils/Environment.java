package frameworks.utils;

import static java.lang.System.getProperty;

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
}
