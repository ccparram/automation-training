package frameworks.config;

import frameworks.web.Browser;

import java.util.Set;

/**
 * @author Juan Krzemien
 */
public interface Config {

    boolean isDebugMode();

    WebDriver WebDriver();

    Driver Driver(Browser browser);

    Proxy Proxy();

    Set<Browser> AvailableDrivers();

}
