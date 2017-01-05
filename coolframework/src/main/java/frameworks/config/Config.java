package frameworks.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import frameworks.web.Browser;

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
class Config {

    @JsonProperty
    private boolean isDebugMode = false;

    @JsonProperty
    private WebDriver webdriver = new WebDriver();

    @JsonProperty
    private Map<Browser, Driver> drivers = new HashMap<>();

    @JsonProperty
    private Proxy proxy = new Proxy();

    Config() {
    }

    public boolean isDebugMode() {
        return parseBoolean(getProperty("DEBUG_MODE", valueOf(isDebugMode)));
    }

    public WebDriver WebDriver() {
        return webdriver;
    }

    public Driver Driver(Browser browser) {
        return drivers.computeIfAbsent(browser, k -> new Driver());
    }

    public Proxy Proxy() {
        return proxy;
    }

    public Set<Browser> AvailableDrivers() {
        return drivers.keySet();
    }
}
