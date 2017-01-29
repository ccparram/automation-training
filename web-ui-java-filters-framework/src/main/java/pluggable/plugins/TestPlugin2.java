package pluggable.plugins;

import com.globant.automation.trainings.logging.Logging;
import pluggable.PageObject;
import pluggable.plugin.Plugin;

/**
 * @author Juan Krzemien
 */
public class TestPlugin2 implements Plugin, Logging {

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    @Override
    public void load() {
        getLogger().info("INITIALIZING PLUGIN " + getClass().getName());
    }

    @Override
    public void unload() {
        getLogger().info("FINALIZING PLUGIN " + getClass().getName());
    }

    @Override
    public Object execute(Object input) {
        getLogger().info("EXECUTING PLUGIN " + getClass().getName());
        if (input instanceof PageObject) {
            ((PageObject)input).setY(1983);
        }
        return input;
    }

    @Override
    public int compareTo(Plugin o) {
        return getPriority().compareTo(o.getPriority());
    }
}
