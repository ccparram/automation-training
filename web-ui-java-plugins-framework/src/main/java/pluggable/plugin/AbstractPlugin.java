package pluggable.plugin;

import com.globant.automation.trainings.logging.Logging;


/**
 * @author Juan Krzemien
 */
public abstract class AbstractPlugin<T> implements Comparable<AbstractPlugin<T>>, Logging {

    protected void load() {
        getLogger().info("Initializing plugin...");
    }

    public abstract T execute(T input);

    protected void unload() {
        getLogger().info("Terminating plugin...");
    }

    public abstract Priority getPriority();

    @Override
    public int compareTo(AbstractPlugin<T> o) {
        if (o == null) {
            return 0;
        }
        int result = getPriority().compareTo(o.getPriority());
        if (result == 0) {
            int myHash = System.identityHashCode(this);
            int itsHash = System.identityHashCode(o);
            if (myHash != itsHash) {
                result = getClass().getSimpleName().compareTo(o.getClass().getSimpleName());
            }
        }
        return result;
    }
}
