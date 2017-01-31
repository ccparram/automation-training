package pluggable.plugin;

import com.globant.automation.trainings.logging.Logging;

/**
 * @author Juan Krzemien
 */
public abstract class AbstractPlugin implements Plugin, Logging {

    @Override
    public int compareTo(Plugin o) {
        return getPriority().compareTo(o.getPriority());
    }

    @Override
    public void load() {
        getLogger().info("Initializing plugin...");
    }

    @Override
    public void unload() {
        getLogger().info("Finalizing plugin...");
    }

}
