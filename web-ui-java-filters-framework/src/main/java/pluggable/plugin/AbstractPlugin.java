package pluggable.plugin;

import com.globant.automation.trainings.logging.Logging;


/**
 * @author Juan Krzemien
 */
public abstract class AbstractPlugin<T> implements Comparable<AbstractPlugin<T>>, Logging {

    public void load() {
        getLogger().info("Initializing plugin...");
    }

    public abstract T execute(T input);

    public void unload() {
        getLogger().info("Terminating plugin...");
    }

    public abstract Priority getPriority();

    @Override
    public int compareTo(AbstractPlugin<T> o) {
        return getPriority().compareTo(o.getPriority());
    }
}
