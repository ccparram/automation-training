package pluggable.plugins;


import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.DesiredCapabilities;
import pluggable.plugin.AbstractPlugin;

import static com.globant.automation.trainings.webdriver.config.Framework.CONFIGURATION;
import static java.lang.String.format;
import static org.openqa.selenium.remote.CapabilityType.PROXY;

/**
 * @author Juan Krzemien
 */
public class WebDriverProxyPlugin extends AbstractPlugin {

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }

    @Override
    public Object execute(Object input) {
        if (input instanceof DesiredCapabilities && CONFIGURATION.Proxy().isEnabled()) {
            getLogger().info(format("Executing %s plugin...", getClass().getName()));
            DesiredCapabilities capabilities = (DesiredCapabilities) input;
            getLogger().info("Setting WebDriver proxy...");

            String proxyCfg = CONFIGURATION.Proxy().getHost() + ":" + CONFIGURATION.Proxy().getPort();

            capabilities.setCapability(PROXY, new Proxy()
                    .setHttpProxy(proxyCfg)
                    .setFtpProxy(proxyCfg)
                    .setSslProxy(proxyCfg)
            );

            return capabilities;
        }
        return input;
    }
}
