package pluggable.plugin;

/**
 * @author Juan Krzemien
 */
public interface Plugin extends Comparable<Plugin> {

    Priority getPriority();

    void load();

    void unload();

    Object execute(Object request);

    enum Priority {
        HIGH, MEDIUM, LOW
    }
}
