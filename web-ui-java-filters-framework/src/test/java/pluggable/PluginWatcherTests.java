package pluggable;

import com.globant.automation.trainings.logging.Logging;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Juan Krzemien
 */
public class PluginWatcherTests implements Logging {

    @Test
    public void aFolderWatcherTest() throws Exception {

        try (ObjectsContainer objectsContainer = ObjectsContainer.INSTANCE) {

            PageObject pom = new PageObject();

            objectsContainer.add(pom);

            getLogger().info("New POM X value: " + objectsContainer.get(PageObject.class).getX());
            getLogger().info("New POM Y value: " + objectsContainer.get(PageObject.class).getY());

            TimeUnit.SECONDS.sleep(15);

        }
    }
}
