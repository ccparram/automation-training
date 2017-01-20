package frameworks.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import frameworks.web.Browser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;
import static java.lang.System.getProperty;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;

/**
 * @author Juan Krzemien
 */
@JsonSerialize
class ConfigImpl implements Config {

    @JsonProperty
    private boolean isDebugMode = false;

    @JsonDeserialize(as = WebDriverImpl.class)
    private WebDriver webdriver = new WebDriverImpl();

    @JsonDeserialize(contentAs = DriverImpl.class)
    private Map<Browser, Driver> drivers = new HashMap<>();

    @JsonDeserialize(as = ProxyImpl.class)
    private Proxy proxy = new ProxyImpl();

    ConfigImpl() {
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
    public Driver Driver(Browser browser) {
        return ofNullable(drivers).orElse(emptyMap()).computeIfAbsent(browser, k -> new DriverImpl());
    }

    @Override
    public Proxy Proxy() {
        return proxy;
    }

    @Override
    public Set<Browser> AvailableDrivers() {
        return drivers.keySet();
    }
}
