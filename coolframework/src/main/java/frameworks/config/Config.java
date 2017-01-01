package frameworks.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import frameworks.config.interfaces.IConfig;
import frameworks.config.interfaces.IDriver;
import frameworks.config.interfaces.IProxy;
import frameworks.web.Browser;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;
import static java.lang.System.getProperty;

/**
 * @author Juan Krzemien
 */
@JsonSerialize
class Config implements IConfig {

    @JsonProperty
    private boolean isDebugMode = false;

    @JsonProperty
    private WebDriver webdriver = new WebDriver();

    @JsonProperty
    private Map<Browser, Driver> drivers = new HashMap<>();

    @JsonProperty
    private Proxy proxy = new Proxy();

    Config() throws MalformedURLException {
    }

    @Override
    public boolean isDebugMode() {
        return parseBoolean(getProperty("DEBUG_MODE", valueOf(isDebugMode)));
    }

    @Override
    public WebDriver WebDriver() {
        return webdriver;
    }

    @Override
    public IDriver Driver(Browser browser) {
        return drivers.computeIfAbsent(browser, k -> new Driver());
    }

    @Override
    public IProxy Proxy() {
        return proxy;
    }

    public Set<Browser> AvailableDrivers() {
        return drivers.keySet();
    }
}
