package pluggable;

import com.globant.automation.trainings.logging.Logging;
import org.junit.Test;
import pluggable.filter.FilterChain;
import pluggable.filter.impl.FilterChainImpl;
import pluggable.plugin.impl.PluginManagerImpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * @author Juan Krzemien
 */
public class PluginWatcherTests implements Logging {

    private static final Path PLUGINS_DIRECTORY = Paths.get("plugins");

    @Test
    public void aFolderWatcherTest() throws Exception {

        try (PluginManagerImpl pluginsManager = new PluginManagerImpl(PLUGINS_DIRECTORY)) {

            FilterChain filterChain = new FilterChainImpl(pluginsManager);

            PageObject pom = new PageObject();
            filterChain.processFilter(pom);

            getLogger().info("New POM X value: " + pom.getX());
            getLogger().info("New POM Y value: " + pom.getY());

            TimeUnit.SECONDS.sleep(15);

        }
    }
}
