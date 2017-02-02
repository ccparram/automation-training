package pluggable.plugins;


import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.DesiredCapabilities;
import pluggable.plugin.AbstractPlugin;
import pluggable.plugin.Priority;

import static com.globant.automation.trainings.webdriver.config.Framework.CONFIGURATION;
import static org.openqa.selenium.remote.CapabilityType.PROXY;
import static pluggable.plugin.Priority.LOW;

/**
 * @author Juan Krzemien
 */
public class WebDriverProxyPlugin extends AbstractPlugin<DesiredCapabilities> {

    @Override
    public Priority getPriority() {
        return LOW;
    }

    @Override
    public DesiredCapabilities execute(DesiredCapabilities input) {
        getLogger().info("Setting WebDriver proxy...");

        String proxyCfg = CONFIGURATION.Proxy().getHost() + ":" + CONFIGURATION.Proxy().getPort();

        input.setCapability(PROXY, new Proxy()
                .setHttpProxy(proxyCfg)
                .setFtpProxy(proxyCfg)
                .setSslProxy(proxyCfg)
        );

        return input;
    }

}
